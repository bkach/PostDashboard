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

package com.example.boris.postdashboard.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.boris.postdashboard.R
import com.example.boris.postdashboard.viewmodel.DashboardViewModel
import com.example.boris.postdashboard.viewmodel.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    // ViewModel must be injected here to allow the same instance to be shared with the fragments
    private val viewModel: DashboardViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            return
        }

        val host = NavHostFragment.create(R.navigation.nav_graph)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_fragment_container, host)
            .setPrimaryNavigationFragment(host)
            .commit()

        logErrors()
    }

    // TODO: Include errors in any future analytics
    private fun logErrors() {
        viewModel.state.observe(this,
            Observer<State> { state ->
                when(state) {
                    is State.Error -> Log.e(javaClass.simpleName, state.message)
                }
            })
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is NavHostFragment) {
                it.childFragmentManager.fragments.forEach {
                    if (it is DetailFragment) {
                        it.onBackPressed()
                    }
                }
            }
        }
        super.onBackPressed()
    }
}
