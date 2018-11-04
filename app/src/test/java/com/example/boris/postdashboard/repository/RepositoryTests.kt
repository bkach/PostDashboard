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
import kotlinx.coroutines.CompletableDeferred
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

            assertEquals(Result.LoadDetailsResult(post), result)
        }
    }

    @Test
    fun `when getting details, return error result if user results fails`() {
        runBlocking {
            whenever(databaseRepository.getUsers(anyOrNull(), anyOrNull())).thenReturn(
                Repository.UsersResult.UserLoadingError
            )

            whenever(databaseRepository.getComments(anyOrNull(), anyOrNull())).thenReturn(
                Repository.CommentsResult.CommentsLoadedResult(comments)
            )

            val result = repository.getDetails(post).await()

            assertEquals(Result.DetailsLoadingError, result)
        }
    }

    @Test
    fun `when getting details, return error result if comments results fails`() {
        runBlocking {
            whenever(databaseRepository.getUsers(anyOrNull(), anyOrNull())).thenReturn(
                Repository.UsersResult.UsersLoadedResult(users)
            )

            whenever(databaseRepository.getComments(anyOrNull(), anyOrNull())).thenReturn(
                Repository.CommentsResult.CommentsLoadingError
            )

            val result = repository.getDetails(post).await()

            assertEquals(Result.DetailsLoadingError, result)
        }
    }

    @Test
    fun `when updating a post, its user name and comments should be updated`() {

        val updatedPost = repository.createUpdatedPost(post, users, comments)

        assertEquals(2, updatedPost.numComments)
        assertEquals("Anne", post.userName)
    }
}