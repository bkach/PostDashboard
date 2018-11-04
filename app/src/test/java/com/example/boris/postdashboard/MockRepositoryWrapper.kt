package com.example.boris.postdashboard

import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.repository.Repository
import com.example.boris.postdashboard.viewmodel.Result
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking

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
                Result.LoadDetailsResult(
                    mockPost
                )
            )
        }}
    }

    private fun mockGetPosts() {
        whenever(repository.getPosts()).then { runBlocking {
            CompletableDeferred(
                Result.LoadPostsResult(
                    listOf(mockPost)
                )
            )
        }}
    }

    fun mockError() {
        whenever(repository.getPosts()).then { runBlocking {
            CompletableDeferred(
                Result.PostLoadingError
            )
        }}
    }
}