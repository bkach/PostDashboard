package com.example.boris.postdashboard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(val userId: String, @PrimaryKey val id: Int, val title: String, val body: String,
                var userName: String?, var numComments: Int?)
