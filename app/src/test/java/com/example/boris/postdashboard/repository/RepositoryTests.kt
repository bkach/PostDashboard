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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.boris.postdashboard.TestContextProvider
import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User
import com.example.boris.postdashboard.viewmodel.Result
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class RepositoryTests {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: Repository

    private val databaseRepository = mock<DatabaseRepository>()
    private val networkRepository = mock<NetworkRepository>()

    private val post = Post(1, 1, "title", "body", null, null)
    private val users = listOf(
        User(1, "Anne"),
        User(2, "Jenny")
    )
    private val comments = listOf(
        Comment(1, 1),
        Comment(2, 1)
    )

    @Before
    fun setup() {
        repository = Repository(databaseRepository, networkRepository, TestContextProvider())

    }

    @Test
    fun `when getting details, return detail result if user and comments results were successful`() {
        runBlocking {
            whenever(databaseRepository.getUsers(anyOrNull(), anyOrNull())).thenReturn(
                    Repository.UsersResult.UsersLoadedResult(users)
            )

            whenever(databaseRepository.getComments(anyOrNull(), anyOrNull())).thenReturn(
                Repository.CommentsResult.CommentsLoadedResult(comments)
            )

            val result = repository.getDetails(post).await()

            assertEquals(Result.DetailsLoadResult(post), result)
        }
    }

    @Test
    fun `when getting details, return error result if user results fails`() {
        runBlocking {
            whenever(databaseRepository.getUsers(anyOrNull(), anyOrNull())).thenReturn(
                Repository.UsersResult.UserLoadingError("Error")
            )

            whenever(databaseRepository.getComments(anyOrNull(), anyOrNull())).thenReturn(
                Repository.CommentsResult.CommentsLoadedResult(comments)
            )

            val result = repository.getDetails(post).await()

            assertEquals(Result.DetailsLoadingError("Error"), result)
        }
    }

    @Test
    fun `when getting details, return error result if comments results fails`() {
        runBlocking {
            whenever(databaseRepository.getUsers(anyOrNull(), anyOrNull())).thenReturn(
                Repository.UsersResult.UsersLoadedResult(users)
            )

            whenever(databaseRepository.getComments(anyOrNull(), anyOrNull())).thenReturn(
                Repository.CommentsResult.CommentsLoadingError("Error")
            )

            val result = repository.getDetails(post).await()

            assertEquals(Result.DetailsLoadingError("Error"), result)
        }
    }

    @Test
    fun `when updating a post, its user name and comments should be updated`() {

        val updatedPost = repository.createUpdatedPost(post, users, comments)

        assertEquals(2, updatedPost.numComments)
        assertEquals("Anne", post.userName)
    }
}