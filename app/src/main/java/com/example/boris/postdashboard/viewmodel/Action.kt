package com.example.boris.postdashboard.viewmodel

import com.example.boris.postdashboard.model.Post
import kotlinx.coroutines.Deferred
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

sealed class Action {
    object LoadPostsAction : Action()
    data class ShowDetailViewAction(val selectedPost: Post): Action()

    class ActionInterpreter : Interpreter<Action, Deferred<Result>>(), KoinComponent {
        val loadPostsProcessor: LoadPostsProcessor by inject()
        val showDetailProcessor: ShowDetailProcessor by inject()

        override fun interpret(input: Action): Deferred<Result> =
            when(input) {
                is LoadPostsAction -> loadPostsProcessor.load()
                is ShowDetailViewAction -> showDetailProcessor.load(input.selectedPost)
            }
    }
}