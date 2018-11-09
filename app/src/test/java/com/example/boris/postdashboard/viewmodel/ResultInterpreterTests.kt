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

            resultInterpreter.interpret(Result.PostsLoadResult(listOf(mockRepositoryWrapper.mockPost)),
                callback)
        }
    }

    // TODO: Test the remaining Result -> State functions - currently they are trivial and the functions would look quite similar to the one above
}

