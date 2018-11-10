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

import androidx.room.Embedded
import androidx.room.Relation

/**
 * A class which uses relations to group related tables, and the primary way in which the model interacts with the
 * rest of the app architecture.
 */
class PostWithMetadata {

    @Embedded
    var post: Post? = null

    @Relation(parentColumn = "id", entityColumn = "postId", entity = Comment::class)
    var commentList: List<Comment>? = null

    @Relation(parentColumn = "userId", entityColumn = "id", entity = User::class)
    var userList: List<User>? = null

    @Relation(parentColumn = "id", entityColumn = "id", entity = Photo::class)
    var photoList: List<Photo>? = null
}