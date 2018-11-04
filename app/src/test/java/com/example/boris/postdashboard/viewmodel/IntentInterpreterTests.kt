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