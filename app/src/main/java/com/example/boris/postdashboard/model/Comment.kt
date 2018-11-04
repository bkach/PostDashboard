package com.example.boris.postdashboard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comment(@PrimaryKey val id: Int, val postId: Int)
