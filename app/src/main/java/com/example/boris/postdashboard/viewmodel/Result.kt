package com.example.boris.postdashboard.viewmodel

import com.example.boris.postdashboard.model.Post

sealed class Result {
    data class LoadPostsResult(val posts: List<Post>): Result()
    data class LoadDetailsResult(val post: Post): Result()
    object PostLoadingError : Result()
    object PostsLoading : Result()
    object DetailsLoading : Result()
    object DetailsLoadingError : Result()

    class ResultInterpreter : Interpreter<Result, State>() {
        override suspend fun interpret(input: Result, callback: suspend (State) -> Unit) {
            callback(
                when(input) {
                    is LoadPostsResult -> State.PostsLoaded(input.posts)
                    is PostsLoading -> State.PostsLoading
                    is DetailsLoading -> State.DetailsLoading
                    is PostLoadingError -> State.Error
                    is LoadDetailsResult -> State.DetailsLoaded(input.post)
                    is DetailsLoadingError -> State.Error
                }
            )
        }
    }
}