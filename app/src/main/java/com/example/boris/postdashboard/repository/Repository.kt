/*
 * PostDashboard
 * Copyright (C) 2018 Boris Kachscovsky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.example.boris.postdashboard.repository

import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User
import com.example.boris.postdashboard.viewmodel.CoroutineContextProvider
import com.example.boris.postdashboard.viewmodel.Result
import com.example.boris.postdashboard.viewmodel.Result.*
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import kotlin.coroutines.CoroutineContext

class Repository constructor(
    private val databaseRepository: DatabaseRepository,
    private val networkRepository: NetworkRepository,
    private val contextPool: CoroutineContextProvider
) : CoroutineScope, KoinComponent {

    override val coroutineContext: CoroutineContext
        get() = contextPool.IO

    // TODO: There's a fair bit of code duplication here - could this be done in a better way?

    fun getPosts(): Deferred<Result> = async {
        databaseRepository.getPosts({ LoadPostsResult(it) }) {
            networkRepository.getPosts({
                databaseRepository.savePosts(it)
                databaseRepository.getPosts( { LoadPostsResult(it) }) {
                    PostLoadingError
                }
            }) {
                PostLoadingError
            }
        }
    }

    private fun getUsers(): Deferred<UsersResult> = async {
        databaseRepository.getUsers( { UsersResult.UsersLoadedResult(it) } ) {
            networkRepository.getUsers({
                databaseRepository.saveUsers(it)
                databaseRepository.getUsers( { UsersResult.UsersLoadedResult(it) } ) {
                    UsersResult.UserLoadingError
                }
            }) {
                UsersResult.UserLoadingError
            }
        }
    }

    private fun getComments(): Deferred<CommentsResult> = async {
        databaseRepository.getComments( { CommentsResult.CommentsLoadedResult(it) } ) {
            networkRepository.getComments({
                databaseRepository.saveComments(it)
                databaseRepository.getComments( { CommentsResult.CommentsLoadedResult(it) } ) {
                    CommentsResult.CommentsLoadingError
                }
            }) {
                CommentsResult.CommentsLoadingError
            }
        }
    }

    fun getDetails(selectedPost: Post)  = async {
        val userResult: UsersResult = getUsers().await()
        val commentsResult: CommentsResult = getComments().await()

        when (userResult) {
            is UsersResult.UsersLoadedResult -> when (commentsResult) {
                is CommentsResult.CommentsLoadedResult ->
                    Result.LoadDetailsResult(
                        createUpdatedPost(selectedPost, userResult.users, commentsResult.comments)
                    )
                else -> DetailsLoadingError
            }
            else -> DetailsLoadingError
        }
    }

    fun createUpdatedPost(selectedPost: Post,
                                  users: List<User>, comments: List<Comment>) : Post {
        val user = users.find { it.id == selectedPost.userId }
        val numComments = comments.filter { it.postId == selectedPost.id }.size

        selectedPost.userName = user?.name
        selectedPost.numComments = numComments

        return selectedPost
    }

    sealed class UsersResult {
        data class UsersLoadedResult(val users: List<User>): UsersResult()
        object UserLoadingError: UsersResult()
    }

    sealed class CommentsResult {
        data class CommentsLoadedResult(val comments: List<Comment>): CommentsResult()
        object CommentsLoadingError: CommentsResult()
    }
}