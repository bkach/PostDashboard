package com.example.boris.postdashboard

import android.app.Application
import androidx.room.Room
import com.example.boris.postdashboard.repository.*
import com.example.boris.postdashboard.viewmodel.Action
import com.example.boris.postdashboard.viewmodel.Intent
import com.example.boris.postdashboard.viewmodel.Result
import com.example.boris.postdashboard.viewmodel.ViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(listOf(module {
            single { Intent.IntentInterpreter() }
            single { Action.ActionInterpreter() }
            single { Result.ResultInterpreter() }
            single { NetworkRepository(RetrofitWrapper.getJsonPlaceholderService())}
            single { DatabaseRepository(PostDatabaseFactory(applicationContext).createDatabase()) }
            single { Repository(get(), get()) }
            viewModel { ViewModel(get(), get(), get(), get()) }
        }))
    }
}