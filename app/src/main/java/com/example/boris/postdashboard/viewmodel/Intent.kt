package com.example.boris.postdashboard.viewmodel

import com.example.boris.postdashboard.model.Post

sealed class Intent {
    object InitialIntent : Intent()
    data class SelectPostIntent(val selectedPost: Post) : Intent()

    class IntentInterpreter : Interpreter<Intent, Action>() {
        override suspend fun interpret(input: Intent, callback: suspend (Action) -> Unit) {
            callback(
                when (input) {
                    is InitialIntent -> Action.LoadPostsAction
                    is SelectPostIntent -> Action.ShowDetailViewAction(input.selectedPost)
                }
            )
        }
    }
}
