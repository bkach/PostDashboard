package com.example.boris.postdashboard.repository

import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User
import com.example.boris.postdashboard.viewmodel.Result

class NetworkRepository constructor(val service: RetrofitWrapper.JsonPlaceholderService) {

    suspend fun getPosts(success: suspend (List<Post>) -> Result, error: () -> Result) : Result {
        val postsResponse = service.getPosts().await()

        return if (postsResponse.isSuccessful && postsResponse.body() != null) {
            success(postsResponse.body()!!)
        } else {
            error()
        }
    }

    suspend fun getUsers(success: suspend (List<User>) -> Repository.UsersResult,
                         error: () -> Repository.UsersResult) : Repository.UsersResult{
        val usersResponse = service.getUsers().await()

        return if (usersResponse.isSuccessful && usersResponse.body() != null) {
            success(usersResponse.body()!!)
        } else {
            error()
        }
    }

    suspend fun getComments(success: suspend (List<Comment>) -> Repository.CommentsResult,
                            error: () -> Repository.CommentsResult) : Repository.CommentsResult{
        val commentsResponse = service.getComments().await()

        return if (commentsResponse.isSuccessful && commentsResponse.body() != null) {
            success(commentsResponse.body()!!)
        } else {
            error()
        }
    }
}