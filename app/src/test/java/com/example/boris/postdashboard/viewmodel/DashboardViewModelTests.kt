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
import com.example.boris.postdashboard.mocks.MockRepositoryWrapper
import com.example.boris.postdashboard.TestContextProvider
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockMetadata
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class DashboardViewModelTests {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: DashboardViewModel

    private val repositoryWrapper = MockRepositoryWrapper()
    private val intentInterpreter = Intent.IntentInterpreter()
    private val actionInterpreter = Action.ActionInterpreter(repositoryWrapper.repository)
    private val resultInterpreter = Result.ResultInterpreter()
    private val testContextProvider = TestContextProvider()

    @Before
    fun setup() {
        viewModel = DashboardViewModel(intentInterpreter, actionInterpreter, resultInterpreter, testContextProvider)
    }

    @Test
    fun `on init, send initial load post intent`() {
        val mockIntentInterpreter: Intent.IntentInterpreter = mock()
        viewModel = DashboardViewModel(mockIntentInterpreter, actionInterpreter, resultInterpreter, testContextProvider)

        runBlocking {
            verify(mockIntentInterpreter).interpret(eq(Intent.LoadPostData), anyOrNull())
        }
    }

    @Test
    fun `on init, recieve initial data`() {
        viewModel.state.observeForever {
            assertEquals(State.PostsLoaded(mockMetadata), it)
        }
    }

    @Test
    fun `when sending leave detail intent, get post data`() {
        viewModel.sendIntent(Intent.LeaveDetailIntent)
        viewModel.state.observeForever {
            assertEquals(State.PostsLoaded(mockMetadata), it)
        }
    }

    @Test
    fun `when sending comment tapped action, receive hide comment state`() {
        viewModel.sendIntent(Intent.CommentTapped(true))
        viewModel.state.observeForever {
            assertEquals(State.HideComments, it)
        }
    }

    @Test
    fun `when post selected, return details loaded`() {
        viewModel.sendIntent(Intent.SelectPostIntent(mockMetadata[0]))
        viewModel.state.observeForever {
            assertEquals(State.DetailsLoaded(mockMetadata[0]), it)
        }
    }

    @Test
    fun `when view model cleared, cancel running job`() {
        viewModel.onCleared()
        assert(testContextProvider.jobCanceled)
    }
}