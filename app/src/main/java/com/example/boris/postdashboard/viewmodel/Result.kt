package com.example.boris.postdashboard.viewmodel

import com.example.boris.postdashboard.model.Post

sealed class Result {
    data class LoadPostsResult(val posts: List<Post>?): Result()

    class ResultInterpreter : Interpreter<Result, State>() {
        override fun interpret(input: Result): State =
                when(input) {
                    is LoadPostsResult -> {
                        if (input.posts != null) {
                            State.PostsLoaded(input.posts)
                        } else {
                            State.PostsLoadingError
                        }
                    }
                }
    }
}