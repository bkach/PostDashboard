package com.example.boris.postdashboard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comment(@PrimaryKey val postId: Int)
