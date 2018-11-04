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

import android.app.Application
import com.example.boris.postdashboard.repository.*
import com.example.boris.postdashboard.viewmodel.*
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin

/**
 * Class used primarily for creating and holding the Koin Module for Dependency Injection
 */
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(listOf(module {
            single { Intent.IntentInterpreter() }
            single { Action.ActionInterpreter(get()) }
            single { Result.ResultInterpreter() }
            single { NetworkRepository(RetrofitWrapper.getJsonPlaceholderService())}
            single { DatabaseRepository(PostDatabaseFactory(applicationContext).createDatabase()) }
            single { Repository(get(), get(), get()) }
            single { CoroutineContextProvider() }
            viewModel { DashboardViewModel(get(), get(), get(), get()) } }))
    }
}