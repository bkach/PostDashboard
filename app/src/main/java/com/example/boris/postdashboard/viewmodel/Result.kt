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

import com.example.boris.postdashboard.model.PostWithMetadata

/**
 * The Result of an [Action]s operation
 */
sealed class Result {
    data class PostsLoadResult(val posts: List<PostWithMetadata>): Result()
    data class DetailsLoadResult(val post: PostWithMetadata): Result()
    data class PostsLoadingError(val message: String) : Result()
    data class DetailsLoadingError(val message: String) : Result()
    object PostsLoading : Result()
    object NavigateToDetails : Result()
    data class HideComments(val post: PostWithMetadata) : Result()
    data class ShowComments(val post: PostWithMetadata) : Result()

    /**
     * Maps [Result]s to [State]s
     *
     * Note that same results end up with the same state, namely [State.Error]
     */
    class ResultInterpreter : Interpreter<Result, State>() {
        override suspend fun interpret(input: Result, callback: suspend (State) -> Unit) {
            callback(
                when(input) {
                    is PostsLoadResult -> State.PostsLoaded(input.posts)
                    is PostsLoading -> State.PostsLoading
                    is NavigateToDetails -> State.DetailsLoading
                    is PostsLoadingError -> State.Error(input.message)
                    is DetailsLoadResult -> State.DetailsLoaded(input.post)
                    is DetailsLoadingError -> State.Error(input.message)
                    is HideComments -> State.HideComments(input.post)
                    is ShowComments -> State.ShowComments(input.post)
                }
            )
        }
    }
}