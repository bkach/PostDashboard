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
import com.example.boris.postdashboard.TestContextProvider
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
    fun `when sending the initial intent, return posts loaded state`() {
        viewModel.sendIntent(Intent.InitialIntent)
        viewModel.state.observeForever {
            assertEquals(State.PostsLoaded(listOf(repositoryWrapper.mockPost)), it)
        }
    }

    @Test
    fun `when sending the select post intent, return loaded state`() {
        viewModel.sendIntent(Intent.SelectPostIntent(repositoryWrapper.mockPost))
        viewModel.state.observeForever {
            assertEquals(State.DetailsLoaded(repositoryWrapper.mockPost), it)
        }
    }

    @Test
    fun `when error occurs, show error state`() {
        repositoryWrapper.mockError()
        viewModel.sendIntent(Intent.InitialIntent)
        viewModel.state.observeForever {
            assertEquals(State.Error("Error"), it)
        }
    }

    @Test
    fun `when view model cleared, cancel running job`() {
        viewModel.onCleared()
        assert(testContextProvider.jobCanceled)
    }
}