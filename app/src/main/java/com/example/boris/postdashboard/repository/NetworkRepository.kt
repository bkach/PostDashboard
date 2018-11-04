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

/**
 * Repository for communicating with the Network
 */
open class NetworkRepository constructor(val service: RetrofitWrapper.JsonPlaceholderService) {

    suspend fun getPosts(success: suspend (List<Post>) -> Result, error: () -> Result) : Result {
        val postsResponse = service.getPosts().await()

        return if (postsResponse.isSuccessful && postsResponse.body() != null) {
            success(postsResponse.body()!!)
        } else {
            error()
        }
    }

    suspend fun getUsers(success: suspend (List<User>) -> Repository.UsersResult,
                         error: () -> Repository.UsersResult) : Repository.UsersResult{
        val usersResponse = service.getUsers().await()

        return if (usersResponse.isSuccessful && usersResponse.body() != null) {
            success(usersResponse.body()!!)
        } else {
            error()
        }
    }

    suspend fun getComments(success: suspend (List<Comment>) -> Repository.CommentsResult,
                            error: () -> Repository.CommentsResult) : Repository.CommentsResult{
        val commentsResponse = service.getComments().await()

        return if (commentsResponse.isSuccessful && commentsResponse.body() != null) {
            success(commentsResponse.body()!!)
        } else {
            error()
        }
    }
}