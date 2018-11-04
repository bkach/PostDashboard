package com.example.boris.postdashboard.viewmodel

import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.repository.Repository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

sealed class Action {
    object LoadPostsAction : Action()
    data class ShowDetailViewAction(val selectedPost: Post): Action()

    class ActionInterpreter(val repository: Repository) : Interpreter<Action, Result>(), KoinComponent {

        override suspend fun interpret(input: Action, callback: suspend (Result) -> Unit) {
            callback(
                when(input) {
                    is LoadPostsAction -> {
                        callback(Result.PostsLoading)
                        repository.getPosts().await()
                    }
                    is ShowDetailViewAction -> {
                        callback(Result.DetailsLoading)
                        repository.getDetails(input.selectedPost).await()
                    }
                }
            )
        }

    }
}