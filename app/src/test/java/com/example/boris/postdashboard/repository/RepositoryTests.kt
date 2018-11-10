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
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockMetadata
import com.example.boris.postdashboard.mocks.MockDatabaseRepository
import com.example.boris.postdashboard.mocks.MockNetworkRepository
import com.example.boris.postdashboard.viewmodel.Result
import com.nhaarman.mockitokotlin2.*
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

    private var databaseRepository = MockDatabaseRepository()
    private var networkRepository = MockNetworkRepository()

    @Before
    fun setup() {
        databaseRepository = MockDatabaseRepository()
        networkRepository = MockNetworkRepository()

        repository = Repository(databaseRepository, networkRepository, TestContextProvider())
    }

    @Test
    fun `when updating posts and database success, return success`() {
        runBlocking {
            val result = repository.updatePosts().await()
            assertEquals(Repository.RequestResult.Success, result)
        }
    }

    @Test
    fun `when updating posts and database failure, return success from network and save posts`() {
        databaseRepository.getPostsSuccess = false
        runBlocking {
            val result = repository.updatePosts().await()
            assertEquals(Repository.RequestResult.Success, result)
            verify(MockDatabaseRepository.mockDao).savePosts(any())
        }
    }

    @Test
    fun `when updating posts and both network and database miss, return error`() {
        databaseRepository.getPostsSuccess = false
        networkRepository.getPostsSuccess = false
        runBlocking {
            val result = repository.updatePosts().await()
            assertEquals(Repository.RequestResult.Error("error"), result)
        }
    }

    // TODO: Test update users, comments, and photos - the tests would look identical to the ones above

    @Test
    fun `when updating data and post results in error, return error`() {
        databaseRepository.getPostsSuccess = false
        networkRepository.getPostsSuccess = false
        runBlocking {
            val result = repository.updateData()
            assertEquals(Result.PostsLoadingError("error"), result)
        }
    }

    // TODO: test if users, comments, and photo updating fails - but again, the tests would be identical

    @Test
    fun `when updating data successfully, updateData should return null`() {
        runBlocking {
            val result = repository.updateData()
            assertEquals(null, result)
        }
    }

    @Test
    fun `when loading posts and data update error, return that error`() {
        databaseRepository.getPostsSuccess = false
        networkRepository.getPostsSuccess = false
        runBlocking {
            val result = repository.loadPosts()
            assertEquals(Result.PostsLoadingError("error"), result)
        }
    }

    @Test
    fun `when loading posts and postsWithMetatada returns error, return that error`() {
        databaseRepository.getPostsWithMetadataSuccess = false
        runBlocking {
            val result = repository.loadPosts()
            assertEquals(Result.PostsLoadingError("Error"), result)
        }
    }

    @Test
    fun `when loading posts and no errors, return posts with metadata`() {
        runBlocking {
            val result = repository.loadPosts()
            assertEquals(Result.PostsLoadResult(mockMetadata), result)
        }
    }

//    @Test
//    fun `when getting details, return detail result if user and comments results were successful`() {
////        runBlocking {
////            whenever(databaseRepository.getUsers(anyOrNull(), anyOrNull())).thenReturn(
////                    Repository.UsersResult.UsersLoadedResult(users)
////            )
////
////            whenever(databaseRepository.getComments(anyOrNull(), anyOrNull())).thenReturn(
////                Repository.CommentsResult.CommentsLoadedResult(comments)
////            )
////
////            val result = repository.getDetails(post).await()
////
////            assertEquals(Result.DetailsLoadResult(post), result)
////        }
//    }
////
////    @Test
////    fun `when getting details, return error result if user results fails`() {
////        runBlocking {
////            whenever(databaseRepository.getUsers(anyOrNull(), anyOrNull())).thenReturn(
////                Repository.UsersResult.UserLoadingError("Error")
////            )
////
////            whenever(databaseRepository.getComments(anyOrNull(), anyOrNull())).thenReturn(
////                Repository.CommentsResult.CommentsLoadedResult(comments)
////            )
////
////            val result = repository.getDetails(post).await()
////
////            assertEquals(Result.DetailsLoadingError("Error"), result)
////        }
////    }
////
////    @Test
////    fun `when getting details, return error result if comments results fails`() {
////        runBlocking {
////            whenever(databaseRepository.getUsers(anyOrNull(), anyOrNull())).thenReturn(
////                Repository.UsersResult.UsersLoadedResult(users)
////            )
////
////            whenever(databaseRepository.getComments(anyOrNull(), anyOrNull())).thenReturn(
////                Repository.CommentsResult.CommentsLoadingError("Error")
////            )
////
////            val result = repository.getDetails(post).await()
////
////            assertEquals(Result.DetailsLoadingError("Error"), result)
////        }
////    }
////
////    @Test
////    fun `when updating a post, its user name and comments should be updated`() {
////
////        val updatedPost = repository.createUpdatedPost(post, users, comments)
////
////        assertEquals(2, updatedPost.numComments)
////        assertEquals("Anne", post.userName)
////    }
}