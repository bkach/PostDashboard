package com.example.boris.postdashboard.repository

import android.content.Context
import androidx.room.Room
import org.koin.standalone.KoinComponent

class PostDatabaseFactory constructor(val context: Context) : KoinComponent {
   fun createDatabase() : PostDatabase {
       return Room.databaseBuilder(context, PostDatabase::class.java, "post-db").build()
   }
}