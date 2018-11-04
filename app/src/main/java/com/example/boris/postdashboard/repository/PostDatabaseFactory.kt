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

import android.content.Context
import androidx.room.Room
import org.koin.standalone.KoinComponent

/**
 * Factory for building a database.
 *
 * This class is meant to abstract database creation from the Application class, where it is injected
 */
class PostDatabaseFactory constructor(val context: Context) : KoinComponent {
   fun createDatabase() : PostDatabase {
       return Room.databaseBuilder(context, PostDatabase::class.java, "post-db").build()
   }
}