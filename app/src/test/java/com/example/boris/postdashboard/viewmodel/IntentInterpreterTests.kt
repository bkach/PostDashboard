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

    private val intentInterpreter = Intent.IntentInterpreter()

    @Test
    fun `When sending load post intent, a load posts action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = {action ->
                assertEquals(Action.LoadPostsAction, action)
            }
            intentInterpreter.interpret(Intent.LoadPostData, callback)
        }
    }

    @Test
    fun `When sending select post intent, a show detail view action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = {action ->
                assertEquals(Action.ShowDetailViewAction(0), action)
            }
            intentInterpreter.interpret(Intent.SelectPostIntent(0), callback)
        }
    }

    @Test
    fun `When sending leave detail intent, a show posts without loading action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = {action ->
                assertEquals(Action.ShowPostsWithoutLoading, action)
            }
            intentInterpreter.interpret(Intent.LeaveDetailIntent, callback)
        }
    }

    @Test
    fun `When sending comment tapped intent, a show or hide comment action should be returned`() {
        runBlocking {
            val callback: suspend (Action) -> Unit = {action ->
                assertEquals(Action.ShowOrHideComment(true), action)
            }
            intentInterpreter.interpret(Intent.CommentTapped(true), callback)
        }
    }
}