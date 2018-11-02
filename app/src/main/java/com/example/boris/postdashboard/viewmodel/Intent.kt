package com.example.boris.postdashboard.viewmodel

import com.example.boris.postdashboard.model.Post

sealed class Intent {
    object InitialIntent : Intent()
    data class SelectPostIntent(val selectedPost: Post) : Intent()

    class IntentInterpreter : Interpreter<Intent, Action>() {
        override fun interpret(input: Intent): Action =
            when(input) {
                is InitialIntent -> Action.LoadPostsAction
                is SelectPostIntent -> Action.ShowDetailViewAction(input.selectedPost)
            }
    }
}
