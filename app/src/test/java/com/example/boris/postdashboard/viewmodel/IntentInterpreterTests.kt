package com.example.boris.postdashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.viewmodel.Action
import com.example.boris.postdashboard.viewmodel.Intent
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class IntentInterpreterTests {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val intentInterpreter = Intent.IntentInterpreter()
    val post: Post = mock()

    @Test
    fun `When sending an initial intent, a load posts action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = {action ->
                assertEquals(Action.LoadPostsAction, action)
            }
            intentInterpreter.interpret(Intent.InitialIntent, callback)
        }
    }

    @Test
    fun `When sending a select post intent, a show detail view action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = {action ->
                assertEquals(Action.ShowDetailViewAction(post), action)
            }
            intentInterpreter.interpret(Intent.SelectPostIntent(post), callback)
        }
    }
}