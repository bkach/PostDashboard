package com.example.boris.postdashboard.viewmodel

import com.example.boris.postdashboard.model.Post

sealed class State {
    object PostsLoadingError : State()
    data class PostsLoaded(val posts: List<Post>) : State()
}