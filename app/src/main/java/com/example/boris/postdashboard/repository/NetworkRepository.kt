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
import com.example.boris.postdashboard.viewmodel.Result
import kotlinx.coroutines.Deferred
import retrofit2.Response

/**
 * Repository for communicating with the Network
 */
open class NetworkRepository constructor(private val service: RetrofitWrapper.JsonPlaceholderService) {

    private suspend fun <T,R> processData(
        deferredData: Deferred<Response<List<T>>>,
        success: suspend (List<T>) -> R,
        error: (String) -> R) : R {

        val data: Response<List<T>>

        try {
            data = deferredData.await()
        } catch (e: Exception) {
            return error("${javaClass.simpleName}: ${e.message}")
        }

        val body = data.body()

        return if (data.isSuccessful && !body.isNullOrEmpty()) {
            success(body)
        } else {
            error("${javaClass.simpleName}: Fetch Data Unsuccessful")
        }
    }

    open suspend fun getPosts(success: suspend (List<Post>) -> Result, error: (String) -> Result) : Result =
        processData(service.getPosts(), success, error)

    open suspend fun getUsers(success: suspend (List<User>) -> Repository.UsersResult,
                         error: (String) -> Repository.UsersResult) : Repository.UsersResult =
        processData(service.getUsers(), success, error)

    open suspend fun getComments(success: suspend (List<Comment>) -> Repository.CommentsResult,
                            error: (String) -> Repository.CommentsResult) : Repository.CommentsResult =
        processData(service.getComments(), success, error)
}