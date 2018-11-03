package com.example.boris.postdashboard.viewmodel

import com.example.boris.postdashboard.model.Post

sealed class State {
    object Error : State()
    data class PostsLoaded(val posts: List<Post>) : State()
    data class DetailsLoaded(val post: Post) : State()
    object PostsLoading : State()
    object DetailsLoading : State()
}