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

package com.example.boris.postdashboard.mocks

import com.example.boris.postdashboard.mocks.MockModel.Companion.mockComment
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockMetadata
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockPhoto
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockPost
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockUser
import com.example.boris.postdashboard.model.*
import com.example.boris.postdashboard.repository.DatabaseRepository
import com.example.boris.postdashboard.repository.PostDao
import com.example.boris.postdashboard.repository.PostDatabase
import com.example.boris.postdashboard.repository.Repository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever

class MockDatabaseRepository(private val database: PostDatabase = mockDatabase()) : DatabaseRepository(database) {

    var getPostsSuccess = true
    var getCommentsSuccess = true
    var getUsersSuccess = true
    var getPostsWithMetadataSuccess = true
    var getPhotoSuccess = true

    companion object {
        val mockDatabase: PostDatabase = mock()
        val mockDao: PostDao = mock()

        fun mockDatabase(): PostDatabase {
            whenever(mockDatabase.postDao()).thenReturn(
                mockDao
            )
            return mockDatabase
        }
    }

    override suspend fun getPosts(
        success: (List<Post>) -> Repository.RequestResult,
        error: suspend (String) -> Repository.RequestResult
    ): Repository.RequestResult {
        return if (getPostsSuccess)
            success(listOf(mockPost))
        else
            error("Error")
    }

    override suspend fun getComments(
        success: (List<Comment>) -> Repository.RequestResult,
        error: suspend (String) -> Repository.RequestResult
    ): Repository.RequestResult {
        return if (getCommentsSuccess)
            success(listOf(mockComment))
        else
            error("Error")
    }

    override suspend fun getUsers(
        success: (List<User>) -> Repository.RequestResult,
        error: suspend (String) -> Repository.RequestResult
    ): Repository.RequestResult {
        return if (getUsersSuccess)
            success(listOf(mockUser))
        else
            error("Error")
    }

    override suspend fun getPhotos(
        success: (List<Photo>) -> Repository.RequestResult,
        error: suspend (String) -> Repository.RequestResult
    ): Repository.RequestResult {
        return if (getPhotoSuccess) {
            success(listOf(mockPhoto))
        }
        else
            error("Error")
    }

    override suspend fun getPostsWithMetadata(
        success: (List<PostWithMetadata>) -> Repository.RequestResult,
        error: suspend (String) -> Repository.RequestResult
    ): Repository.RequestResult {
        return if (getPostsWithMetadataSuccess) {
            success(mockMetadata)
        }
        else
            error("Error")
    }

    override fun getLastLoadedPostIndex(): Int = 0

    override fun savePosts(posts: List<Post>) {
        database.postDao().savePosts(posts)
    }
}

