package com.example.boris.postdashboard.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.viewmodel.Result
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import retrofit2.Response

class NetworkRepositoryTests {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val service = mock<RetrofitWrapper.JsonPlaceholderService>()
    val networkRepository = NetworkRepository(service)

    val post = mock<Post>()

    @Test
    fun `when get posts called successfully, call success function`() {
        whenever(service.getPosts()).thenReturn(
            CompletableDeferred(
                Response.success(listOf(post))
            )
        )

        runBlocking {
            val result = networkRepository.getPosts( {
                Result.LoadPostsResult(it)
            }, {
                Result.PostLoadingError
            } )

            assertEquals(Result.LoadPostsResult(listOf(post)), result)
        }
    }

    @Test
    fun `when get posts called unsuccessfully, call error function`() {
        whenever(service.getPosts()).thenReturn(
            CompletableDeferred(
                Response.error(404, ResponseBody.create(null, "Error"))
            )
        )

        runBlocking {
            val result = networkRepository.getPosts( {
                Result.LoadPostsResult(it)
            }, {
                Result.PostLoadingError
            } )

            assertEquals(Result.PostLoadingError, result)
        }
    }


    // TODO: Get users and get comments work similarly, and tests would be almost identical to the ones above

}