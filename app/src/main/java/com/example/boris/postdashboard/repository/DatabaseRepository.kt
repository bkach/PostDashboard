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
import org.koin.standalone.KoinComponent

/**
 * Repository for communicating with the Room database
 */
open class DatabaseRepository constructor(val database: PostDatabase): KoinComponent {

    suspend fun getPosts(success: (List<Post>) -> Result, error: suspend () -> Result) : Result {
        val posts = database.postDao().loadPosts()

        return if (!posts.isNullOrEmpty()) {
            success(posts)
        } else {
            error()
        }
    }

    suspend fun getUsers(success: (List<User>) -> Repository.UsersResult,
                         error: suspend () -> Repository.UsersResult) : Repository.UsersResult{
        val users = database.postDao().loadUsers()

        return if (!users.isNullOrEmpty()) {
            success(users)
        } else {
            error()
        }
    }

    suspend fun getComments(success: (List<Comment>) -> Repository.CommentsResult,
                            error: suspend () -> Repository.CommentsResult) : Repository.CommentsResult {
        val comments = database.postDao().loadComments()

        return if (!comments.isNullOrEmpty()) {
            success(comments)
        } else {
            error()
        }
    }

    fun savePosts(posts: List<Post>) {
        database.postDao().savePosts(posts)
    }

    fun saveUsers(users: List<User>) {
        database.postDao().saveUsers(users)
    }

    fun saveComments(comments: List<Comment>) {
        database.postDao().saveComments(comments)
    }

}