package com.example.boris.postdashboard.repository

import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User
import com.example.boris.postdashboard.viewmodel.Result
import org.koin.standalone.KoinComponent

class DatabaseRepository constructor(val database: PostDatabase): KoinComponent {

    suspend fun getPosts(success: (List<Post>) -> Result, error: suspend () -> Result) : Result {
        val posts = database.postDao().loadPosts()

        return if (!posts.isNullOrEmpty()) {
            success(posts)
        } else {
            error()
        }
    }

    suspend fun getUsers(success: (List<User>) -> Repository.UsersResult,
                         error: suspend () -> Repository.UsersResult) : Repository.UsersResult{
        val users = database.postDao().loadUsers()

        return if (!users.isNullOrEmpty()) {
            success(users)
        } else {
            error()
        }
    }

    suspend fun getComments(success: (List<Comment>) -> Repository.CommentsResult,
                            error: suspend () -> Repository.CommentsResult) : Repository.CommentsResult {
        val comments = database.postDao().loadComments()

        return if (!comments.isNullOrEmpty()) {
            success(comments)
        } else {
            error()
        }
    }

    fun savePosts(posts: List<Post>) {
        database.postDao().savePosts(posts)
    }

    fun saveUsers(users: List<User>) {
        database.postDao().saveUsers(users)
    }

    fun saveComments(comments: List<Comment>) {
        database.postDao().saveComments(comments)
    }

}