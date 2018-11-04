package com.example.boris.postdashboard.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.viewmodel.Result
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class DatabaseRepositoryTests {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val postDatabase = mock<PostDatabase>()
    val postDao = mock<PostDao>()
    val databaseRepository = DatabaseRepository(postDatabase)

    val post = mock<Post>()

    @Before
    fun setup() {
        whenever(postDatabase.postDao()).thenReturn(postDao)
    }

    @Test
    fun `when get posts called successfully, call success function`() {
        whenever(postDao.loadPosts()).thenReturn(listOf(post))

        runBlocking {
            val result = databaseRepository.getPosts( {
                Result.LoadPostsResult(it)
            }, {
                Result.PostLoadingError
            } )

            assertEquals(Result.LoadPostsResult(listOf(post)), result)
        }
    }

    @Test
    fun `when get posts called unsuccessfully, call error function`() {
        whenever(postDao.loadPosts()).thenReturn(listOf())
        runBlocking {
            val result = databaseRepository.getPosts( {
                Result.LoadPostsResult(it)
            }, {
                Result.PostLoadingError
            } )

            assertEquals(Result.PostLoadingError, result)
        }
    }


    // TODO: Get users and get comments function much in the same way, and unit test would be similar to the one above

}