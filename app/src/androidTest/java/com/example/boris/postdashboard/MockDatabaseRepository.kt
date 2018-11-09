package com.example.boris.postdashboard

import android.content.Context
import androidx.room.Room
import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User
import com.example.boris.postdashboard.repository.DatabaseRepository
import com.example.boris.postdashboard.repository.PostDatabase
import com.example.boris.postdashboard.repository.Repository
import com.example.boris.postdashboard.viewmodel.Result

class MockDatabaseRepository(context: Context) : DatabaseRepository(getMockDatabase(context)) {

    var getPostsSuccess = true
    var getCommentsSuccess = true
    var getUsersSuccess = true

    companion object {
        fun getMockDatabase(context: Context) =
            Room.inMemoryDatabaseBuilder(context, PostDatabase::class.java).build()
    }

    override suspend fun getPosts(success: (List<Post>) -> Result, error: suspend (String) -> Result): Result {
        return if (getPostsSuccess)
            Result.PostsLoadResult(
                listOf(Post(1, 1, "Title", "Body", null, null))
            )
        else
            Result.PostsLoadingError("Get Posts Failed")
    }

    override suspend fun getComments(
        success: (List<Comment>) -> Repository.CommentsResult,
        error: suspend (String) -> Repository.CommentsResult
    ): Repository.CommentsResult {
        return if (getCommentsSuccess)
            Repository.CommentsResult.CommentsLoadedResult(listOf(Comment(1, 1)))
        else
            Repository.CommentsResult.CommentsLoadingError("Get Comments Failed")
    }

    override suspend fun getUsers(
        success: (List<User>) -> Repository.UsersResult,
        error: suspend (String) -> Repository.UsersResult
    ): Repository.UsersResult {
        return if (getUsersSuccess)
            Repository.UsersResult.UsersLoadedResult(listOf(User(1, "hello")))
        else
            Repository.UsersResult.UserLoadingError("Get Users Failed")
    }
}

