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

import com.example.boris.postdashboard.model.PostWithMetadata
import com.example.boris.postdashboard.viewmodel.CoroutineContextProvider
import com.example.boris.postdashboard.viewmodel.Result
import kotlinx.coroutines.CoroutineScope
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

    // TODO: There's a fair bit of code duplication here - could this be done in a better way?

    open fun updatePosts() = async {
        databaseRepository.getPosts({ RequestResult.Success }) {
            networkRepository.getPosts({
                databaseRepository.savePosts(it)
                RequestResult.Success
            }) {
                RequestResult.Error(it)
            }
        }
    }

    open fun updateUsers()= async {
        databaseRepository.getUsers( { RequestResult.Success } ) {
            networkRepository.getUsers({
                databaseRepository.saveUsers(it)
                RequestResult.Success
            }) {
                RequestResult.Error(it)
            }
        }
    }

    open fun updateComments()= async {
        databaseRepository.getComments( { RequestResult.Success } ) {
            networkRepository.getComments({
                databaseRepository.saveComments(it)
                RequestResult.Success
            }) {
                RequestResult.Error(it)
            }
        }
    }

    open fun updatePhotos()= async {
        databaseRepository.getPhotos( { RequestResult.Success } ) {
            networkRepository.getPhotos({
                databaseRepository.savePhotos(it)
                RequestResult.Success
            }) {
                RequestResult.Error(it)
            }
        }
    }

    /**
     * Updates database and retrieves a [PostWithMetadata] object, later passed through to the result
     */
    suspend fun loadPosts(): Result {
        val dataUpdateError = updateData()

        if (dataUpdateError != null) {
            return dataUpdateError
        }

        var postsWithMetadata: List<PostWithMetadata>? = null
        val result = databaseRepository.getPostsWithMetadata(
            { postsWithMetadata = it
                RequestResult.Success } )
            { RequestResult.Error(it) }

        if (result is RequestResult.Error) {
            return Result.PostsLoadingError(result.message)
        }

        return Result.PostsLoadResult(postsWithMetadata!!)
    }

    suspend fun updateData() : Result? {
        val postsResult = updatePosts().await()
        val usersResult = updateUsers().await()
        val commentsResult = updateComments().await()
        val photosResult = updatePhotos().await()


        if (postsResult is RequestResult.Error) {
            return Result.PostsLoadingError(postsResult.message)
        }

        if (usersResult is RequestResult.Error) {
            return Result.PostsLoadingError(usersResult.message)
        }

        if (commentsResult is RequestResult.Error) {
            return Result.PostsLoadingError(commentsResult.message)
        }

        if (photosResult is RequestResult.Error) {
            return Result.PostsLoadingError(photosResult.message)
        }

        return null
    }

    sealed class RequestResult {
        object Success : RequestResult()
        data class Error(val message: String): RequestResult()
    }
}
