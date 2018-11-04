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

package com.example.boris.postdashboard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for a post entry
 *
 * Note user name and the number of comments are nullable, as the initial request
 * to the sever does not contain these two items
 */
@Entity
data class Post(val userId: Int, @PrimaryKey val id: Int, val title: String, val body: String,
                var userName: String?, var numComments: Int?)
