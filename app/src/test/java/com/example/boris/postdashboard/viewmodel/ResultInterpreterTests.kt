package com.example.boris.postdashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.boris.postdashboard.MockRepositoryWrapper
import org.junit.Rule
import org.mockito.junit.MockitoJUnit
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ResultInterpreterTests {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val resultInterpreter = Result.ResultInterpreter()
    private val mockRepositoryWrapper = MockRepositoryWrapper()

    @Test
    fun `when load post results is passed, post loaded state should be returned`() {
        runBlocking {
            val callback: suspend (State) -> Unit = { state ->
                assertEquals(State.PostsLoaded(listOf(mockRepositoryWrapper.mockPost)), state)
            }

            resultInterpreter.interpret(Result.LoadPostsResult(listOf(mockRepositoryWrapper.mockPost)),
                callback)
        }
    }

    // TODO: Test the remaining Result -> State functions - currently they are trivial and the functions would look quite similar to the one above
}

