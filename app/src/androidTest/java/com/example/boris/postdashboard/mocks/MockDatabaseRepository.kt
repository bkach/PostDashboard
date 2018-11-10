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

import android.content.Context
import androidx.room.Room
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockComment
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockMetadata
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockPhoto
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockPost
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockUser
import com.example.boris.postdashboard.model.*
import com.example.boris.postdashboard.repository.DatabaseRepository
import com.example.boris.postdashboard.repository.PostDatabase
import com.example.boris.postdashboard.repository.Repository

class MockDatabaseRepository(context: Context) : DatabaseRepository(
    Companion.getMockDatabase(
        context
    )
) {

    var getPostsSuccess = true
    var getCommentsSuccess = true
    var getUsersSuccess = true
    var getPostsWithMetadataSuccess = true

    companion object {
        // TODO: Mock database test with in-memory database
        fun getMockDatabase(context: Context) =
            Room.inMemoryDatabaseBuilder(context, PostDatabase::class.java).build()
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
        return if (getPostsWithMetadataSuccess) {
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

}

