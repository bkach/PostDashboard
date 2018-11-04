package com.example.boris.postdashboard.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class CoroutineContextProvider {

    private val job = Job()

    open val Main: CoroutineContext by lazy { Dispatchers.Main + job }
    open val IO: CoroutineContext by lazy { Dispatchers.IO + job }

    open fun cancelJob() {
        job.cancel()
    }
}