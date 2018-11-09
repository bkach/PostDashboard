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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.koin.standalone.KoinComponent
import kotlin.coroutines.CoroutineContext

/**
 * Repository used to fetch data from either the database or the network.
 *
 * The general rule is that if data cannot be found in the database, it should resort to a network call and
 * finally an error.
 *
 * All requests for data come from the database to ensure a single source of truth.
 */
open class Repository constructor(
    private val databaseRepository: DatabaseRepository,
    private val networkRepository: NetworkRepository,
    private val contextPool: CoroutineContextProvider
) : CoroutineScope, KoinComponent {

    override val coroutineContext: CoroutineContext
        get() = contextPool.IO

    /**
     * All three get functions work similarly:
     *      - Attempt to get data from the database
     *      - If there is an error, attempt the network
     *      - If the network has an error, return an error
     *      - If the network has a hit, save the data and load from the database
     *      - If loading from the database again does not work, return an error
     */

    // TODO: There's a fair bit of code duplication here - could this be done in a better way?

    open fun getPosts(): Deferred<Result> = async {
        databaseRepository.getPosts({ PostsLoadResult(it) }) {
            networkRepository.getPosts({
                databaseRepository.savePosts(it)
                databaseRepository.getPosts( { PostsLoadResult(it) }) {
                    PostsLoadingError(it)
                }
            }) {
                PostsLoadingError(it)
            }
        }
    }

    open fun getUsers(): Deferred<UsersResult> = async {
        databaseRepository.getUsers( { UsersResult.UsersLoadedResult(it) } ) {
            networkRepository.getUsers({
                databaseRepository.saveUsers(it)
                databaseRepository.getUsers( { UsersResult.UsersLoadedResult(it) } ) {
                    UsersResult.UserLoadingError(it)
                }
            }) {
                UsersResult.UserLoadingError(it)
            }
        }
    }

    open fun getComments(): Deferred<CommentsResult> = async {
        databaseRepository.getComments( { CommentsResult.CommentsLoadedResult(it) } ) {
            networkRepository.getComments({
                databaseRepository.saveComments(it)
                databaseRepository.getComments( { CommentsResult.CommentsLoadedResult(it) } ) {
                    CommentsResult.CommentsLoadingError(it)
                }
            }) {
                CommentsResult.CommentsLoadingError(it)
            }
        }
    }

    /**
     * Gets the details of a [Post]
     *
     * If getting users or comments results in an error, this function will return a [DetailsLoadingError]
     *
     * @return A Deferred [Result] containing a post with a user and a number of comments or a [DetailsLoadingError]
     */
    fun getDetails(selectedPost: Post)  = async {
        val userResult: UsersResult = getUsers().await()
        val commentsResult: CommentsResult = getComments().await()

        when (userResult) {
            is UsersResult.UsersLoadedResult -> when (commentsResult) {
                is CommentsResult.CommentsLoadedResult ->
                    Result.DetailsLoadResult(
                        createUpdatedPost(selectedPost, userResult.users, commentsResult.comments)
                    )
                is CommentsResult.CommentsLoadingError -> DetailsLoadingError(commentsResult.message)
            }
            is UsersResult.UserLoadingError -> DetailsLoadingError(userResult.message)
        }
    }

    /**
     * Updates the selected [Post] with its user name and number of comments
     */
    fun createUpdatedPost(selectedPost: Post, users: List<User>, comments: List<Comment>) : Post {
        val user = users.find { it.id == selectedPost.userId }
        val numComments = comments.filter { it.postId == selectedPost.id }.size

        selectedPost.userName = user?.name
        selectedPost.numComments = numComments

        return selectedPost
    }

    /**
     * Result sealed classes, used to abstract requests for data
     */

    sealed class UsersResult {
        data class UsersLoadedResult(val users: List<User>): UsersResult()
        data class UserLoadingError(val message: String): UsersResult()
    }

    sealed class CommentsResult {
        data class CommentsLoadedResult(val comments: List<Comment>): CommentsResult()
        data class CommentsLoadingError(val message: String): CommentsResult()
    }
}