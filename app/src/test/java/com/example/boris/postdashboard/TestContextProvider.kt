package com.example.boris.postdashboard

import com.example.boris.postdashboard.viewmodel.CoroutineContextProvider
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class TestContextProvider : CoroutineContextProvider() {

    var jobCanceled = false

    override val Main: CoroutineContext = Dispatchers.Unconfined
    override val IO: CoroutineContext = Dispatchers.Unconfined

    override fun cancelJob() {
        jobCanceled = true
    }
}