package com.example.boris.postdashboard.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User

@Database(entities = [Comment::class, Post::class, User::class], version = 1, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}