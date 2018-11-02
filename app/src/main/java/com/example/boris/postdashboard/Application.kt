package com.example.boris.postdashboard

import android.app.Application
import com.example.boris.postdashboard.repository.RetrofitWrapper
import com.example.boris.postdashboard.viewmodel.*
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(listOf(module {
            single { RetrofitWrapper.getJsonPlaceholderService() }
            single { Intent.IntentInterpreter() }
            single { Action.ActionInterpreter() }
            single { Result.ResultInterpreter() }
            single { LoadPostsProcessor() }
            single { ShowDetailProcessor() }
            viewModel { ViewModel(get(), get(), get()) }
        }))
    }
}