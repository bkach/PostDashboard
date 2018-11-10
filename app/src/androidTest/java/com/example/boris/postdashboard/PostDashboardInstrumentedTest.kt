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

package com.example.boris.postdashboard

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.boris.postdashboard.mocks.MockDatabaseRepository
import com.example.boris.postdashboard.mocks.MockJsonPlaceholderService
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockComment
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockMetadata
import com.example.boris.postdashboard.mocks.MockModel.Companion.mockPost
import com.example.boris.postdashboard.repository.DatabaseRepository
import com.example.boris.postdashboard.repository.RetrofitWrapper
import com.example.boris.postdashboard.ui.MainActivity
import com.example.boris.postdashboard.ui.PostListAdapter
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.test.KoinTest

/**
 * Espresso UI test
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class PostDashboardInstrumentedTest: KoinTest, KoinComponent {

    @get:Rule
    var activityRule = ActivityTestRule<MainActivity>(
        MainActivity::class.java, false, false)

    private lateinit var mockDatabaseRepository: MockDatabaseRepository
    private lateinit var mockJsonPlaceholderService: MockJsonPlaceholderService

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun onInit_showRecyclerviewWithAtLeastOneItem() {
        init()
        checkIfAtLeastOneItemInRecyclerView()
    }

    @Test
    fun onInit_showRecyclerviewWithCorrectTitle() {
        init()
        onView(withId(R.id.recyclerview_item_textview_title))
            .check(matches(withText(mockPost.title)))
    }

    @Test
    fun onInit_showRecyclerviewWithCorrectAuthor() {
        init()
        onView(withId(R.id.recyclerview_item_textview_user))
            .check(matches(withText("by ${mockMetadata[0].userList?.get(0)?.name}")))
    }

    @Test
    fun onInit_showRecyclerviewWithCorrectNumComments() {
        init()
        onView(withId(R.id.recyclerview_item_textview_numItems))
            .check(matches(withText(
                "${mockMetadata[0].commentList?.size.toString()} "
            )))
    }

    @Test
    fun onTapItem_showDetailViewWithCorrectTitle() {
        init()
        tapFirstItemInRecyclerView()
        onView(withId(R.id.detail_title_text_view))
            .check(matches(withText(mockPost.title)))
    }

    @Test
    fun onTapItem_andTapComments_showComments() {
        init()
        tapFirstItemInRecyclerView()
        tapComments()
        onView(withId(R.id.detail_comment_recyclerview_body_text))
            .check(matches(withText(mockComment.body)))
    }

    @Test
    fun onInit_andLoadPostsError_showError() {
        init {
            mockJsonPlaceholderService.getPostsSuccess = false
            mockDatabaseRepository.getPostsSuccess = false
        }

        checkIfErrorShown()
    }

    private fun init(runAfterRepositiesCreated: () -> Unit = {}) {
        mockRepositories(runAfterRepositiesCreated)
        activityRule.launchActivity(Intent())
    }

    private fun mockRepositories(runAfterRepositoriesCreated: () -> Unit = {}) {
        mockDatabaseRepository = MockDatabaseRepository(getContext())
        mockJsonPlaceholderService = MockJsonPlaceholderService()

        runAfterRepositoriesCreated()

        loadKoinModules(module {
            single (override = true) { mockDatabaseRepository as DatabaseRepository }
            single (override = true) { mockJsonPlaceholderService as RetrofitWrapper.JsonPlaceholderService }
        })
    }

    private fun tapFirstItemInRecyclerView() =
        onView(withId(R.id.post_list_recyclerview))
            .perform(RecyclerViewActions.actionOnItemAtPosition<PostListAdapter.ViewHolder>(0, click()))

    private fun tapComments() =
        onView(withId(R.id.detail_num_comments_text_view))
            .perform(click())

    private fun checkIfErrorShown() =
        onView(withId(R.id.snackbar_text))
            .check(matches(withText(activityRule.activity.resources.getString(R.string.error_string))))

    private fun getContext() = InstrumentationRegistry.getInstrumentation().context

    private fun checkIfAtLeastOneItemInRecyclerView() {
        onView(withId(R.id.post_list_recyclerview))
            .check(RecyclerViewItemCountAssertion(greaterThan(0)!!))
    }

}
