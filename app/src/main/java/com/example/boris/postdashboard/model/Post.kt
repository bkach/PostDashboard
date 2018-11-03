package com.example.boris.postdashboard.model

data class Post(val userId: String, val id: Int, val title: String, val body: String,
                var userName: String?, var numComments: Int?)
