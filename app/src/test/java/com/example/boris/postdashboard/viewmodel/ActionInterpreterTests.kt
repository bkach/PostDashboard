package com.example.boris.postdashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.boris.postdashboard.MockRepositoryWrapper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class ActionInterpreterTests {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var actionInterpreter: Action.ActionInterpreter
    private val repositoryWrapper = MockRepositoryWrapper()

    @Before
    fun setup() {
        actionInterpreter = Action.ActionInterpreter(repositoryWrapper.repository)
    }

    @Test
    fun `When load posts action is passed, post results should show loading and pass results`() {
        runBlocking {

            var timesCalled = 0

            val callback: suspend (Result) -> Unit = { result ->
                timesCalled += 1
                if (timesCalled == 1) {
                    assertEquals(Result.PostsLoading, result)
                } else {
                    assertEquals(Result.LoadPostsResult(listOf(repositoryWrapper.mockPost)), result)
                }
            }

            actionInterpreter.interpret(Action.LoadPostsAction, callback)
        }
    }

    @Test
    fun `When show detail action is passed, detail results should show loading and pass results`() {
        runBlocking {

            var timesCalled = 0

            val callback: suspend (Result) -> Unit = { result ->
                timesCalled += 1
                if (timesCalled == 1) {
                    assertEquals(Result.DetailsLoading, result)
                } else {
                    assertEquals(Result.LoadDetailsResult(repositoryWrapper.mockPost), result)
                }
            }

            actionInterpreter.interpret(Action.ShowDetailViewAction(repositoryWrapper.mockPost), callback)
        }
    }
}