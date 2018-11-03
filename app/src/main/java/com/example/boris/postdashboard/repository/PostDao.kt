package com.example.boris.postdashboard.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User

@Dao
interface PostDao {
    @Insert(onConflict = REPLACE)
    fun savePosts(posts: List<Post>)

    @Insert(onConflict = REPLACE)
    fun saveUsers(posts: List<User>)

    @Insert(onConflict = REPLACE)
    fun saveComments(posts: List<Comment>)

    @Query("SELECT * FROM post")
    fun loadPosts(): List<Post>

    @Query("SELECT * FROM user")
    fun loadUsers(): List<User>

    @Query("SELECT * FROM comment")
    fun loadComments(): List<Comment>

}