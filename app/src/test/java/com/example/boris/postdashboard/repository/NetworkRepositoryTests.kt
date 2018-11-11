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

package com.example.boris.postdashboard.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.boris.postdashboard.model.Post
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import retrofit2.Response

class NetworkRepositoryTests {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val service = mock<RetrofitWrapper.JsonPlaceholderService>()
    private val networkRepository = NetworkRepository(service)
    val post = mock<Post>()

    @Test
    fun `when processing network data which is fetched successfully, call success`() {
        whenever(service.getPosts()).thenReturn(CompletableDeferred(Response.success(listOf(post))))

        runBlocking {
            val result = networkRepository.processData(
                service.getPosts(),
                { Repository.RequestResult.Success },
                { Repository.RequestResult.Error(it) })

            assertEquals(Repository.RequestResult.Success, result)
        }
    }

    @Test
    fun `when processing database data which is fetched unsuccessfully, call error`() {
        whenever(service.getPosts()).thenReturn(CompletableDeferred(
            Response.error(404, ResponseBody.create(null, ""))))

        runBlocking {
            val result = networkRepository.processData(
                service.getPosts(),
                { Repository.RequestResult.Success },
                { Repository.RequestResult.Error(it) })

            assertEquals(Repository.RequestResult.Error("NetworkRepository: Fetch Data Unsuccessful"), result)
        }
    }

}