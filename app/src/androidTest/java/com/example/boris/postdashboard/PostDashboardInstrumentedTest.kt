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

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.boris.postdashboard.ui.MainActivity
import com.example.boris.postdashboard.ui.PostListAdapter
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.test.KoinTest

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class PostDashboardInstrumentedTest: KoinTest {

    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun onInit_showRecyclerviewWithItems() {
        onView(withId(R.id.post_list_recyclerview))
            .check(RecyclerViewItemCountAssertion(greaterThan(0)!!))
    }

    @Test
    fun onClickItem_showDetailViewWithSameString() {
        val index = 0

        val firstTextView = getPostsFragment().view?.findViewById<TextView>(R.id.recyclerview_item_textview)
        val text = firstTextView?.text

        onView(withId(R.id.post_list_recyclerview))
            .perform(RecyclerViewActions.actionOnItemAtPosition<PostListAdapter.ViewHolder>(index, click()))

        onView(withId(R.id.detail_title_text_view))
            .check(matches(withText(text.toString())))
    }

    fun getPostsFragment(): Fragment {
        return (activityRule.activity as MainActivity).supportFragmentManager.fragments[0]
    }
}
