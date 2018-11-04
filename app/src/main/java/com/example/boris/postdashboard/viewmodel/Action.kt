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

package com.example.boris.postdashboard.viewmodel

import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.repository.Repository
import org.koin.standalone.KoinComponent

sealed class Action {
    object LoadPostsAction : Action()
    data class ShowDetailViewAction(val selectedPost: Post): Action()

    class ActionInterpreter(val repository: Repository) : Interpreter<Action, Result>(), KoinComponent {
        override suspend fun interpret(input: Action, callback: suspend (Result) -> Unit) {
            callback(
                when(input) {
                    is LoadPostsAction -> {
                        callback(Result.PostsLoading)
                        repository.getPosts().await()
                    }
                    is ShowDetailViewAction -> {
                        callback(Result.DetailsLoading)
                        repository.getDetails(input.selectedPost).await()
                    }
                }
            )
        }

    }
}