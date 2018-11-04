package com.example.boris.postdashboard

import android.app.Application
import com.example.boris.postdashboard.repository.*
import com.example.boris.postdashboard.viewmodel.*
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin

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
            viewModel { DashboardViewModel(get(), get(), get(), get()) }
        }))
    }
}