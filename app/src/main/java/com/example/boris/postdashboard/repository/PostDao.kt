/*
 * PostDashboard
 * Copyright (C) 2018 Boris Kachscovsky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.example.boris.postdashboard.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User

/**
 * Data Access Object used with the Room library to access the database
 */
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