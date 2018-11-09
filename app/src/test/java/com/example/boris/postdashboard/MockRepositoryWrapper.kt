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

package com.example.boris.postdashboard

import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.repository.Repository
import com.example.boris.postdashboard.viewmodel.Result
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking

/**
 * Holds a mocked repository and sets up callbacks used to mock repository calls
 */
class MockRepositoryWrapper {

    val repository = mock<Repository>()
    val mockPost = mock<Post>()

    init {
        mockGetDetails()
        mockGetPosts()
    }

    private fun mockGetDetails() {
        whenever(repository.getDetails(anyOrNull())).then { runBlocking {
            CompletableDeferred(
                Result.DetailsLoadResult(
                    mockPost
                )
            )
        }}
    }

    private fun mockGetPosts() {
        whenever(repository.getPosts()).then { runBlocking {
            CompletableDeferred(
                Result.PostsLoadResult(
                    listOf(mockPost)
                )
            )
        }}
    }

    fun mockError() {
        whenever(repository.getPosts()).then { runBlocking {
            CompletableDeferred(
                Result.PostsLoadingError("Error")
            )
        }}
    }
}