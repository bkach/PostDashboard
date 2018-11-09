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
                    assertEquals(Result.PostsLoadResult(listOf(repositoryWrapper.mockPost)), result)
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
                    assertEquals(Result.DetailsLoadResult(repositoryWrapper.mockPost), result)
                }
            }

            actionInterpreter.interpret(Action.ShowDetailViewAction(repositoryWrapper.mockPost), callback)
        }
    }
}