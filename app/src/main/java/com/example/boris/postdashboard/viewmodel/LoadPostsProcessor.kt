package com.example.boris.postdashboard.viewmodel

import com.example.boris.postdashboard.repository.RetrofitWrapper.JsonPlaceholderService
import com.example.boris.postdashboard.viewmodel.Result.LoadPostsResult
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import kotlin.coroutines.CoroutineContext

// TODO: Move networking logic to repository
// TODO: Cancellation by the view model
class LoadPostsProcessor : CoroutineScope, KoinComponent {

    val service: JsonPlaceholderService by inject()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun load(): Deferred<Result> = async {
        val posts = service.getPosts().await()

        withContext(Dispatchers.Main) {
            LoadPostsResult(posts.body())
        }
    }

}