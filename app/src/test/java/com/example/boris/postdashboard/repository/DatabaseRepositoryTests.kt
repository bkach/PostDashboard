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
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit

class DatabaseRepositoryTests {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val postDatabase = mock<PostDatabase>()
    private val postDao = mock<PostDao>()
    private val databaseRepository = DatabaseRepository(postDatabase)
    private val post = mock<Post>()

    @Before
    fun setup() {
        whenever(postDatabase.postDao()).thenReturn(postDao)
    }

    @Test
    fun `when processing database data which is fetched successfully, call success`() {
        whenever(postDao.loadPosts()).thenReturn(listOf(post))

        runBlocking {
            val result = databaseRepository.processData(
                postDatabase.postDao().loadPosts(),
                { Repository.RequestResult.Success },
                { Repository.RequestResult.Error(it) })

            assertEquals(Repository.RequestResult.Success, result)
        }
    }

    @Test
    fun `when processing database data which is fetched unsuccessfully, call error`() {
        whenever(postDao.loadPosts()).thenReturn(listOf())

        runBlocking {
            val result = databaseRepository.processData(
                postDatabase.postDao().loadPosts(),
                { Repository.RequestResult.Success },
                { Repository.RequestResult.Error(it) })

            assertEquals(Repository.RequestResult.Error("DatabaseRepository: Load from Database Error"), result)
        }
    }

}