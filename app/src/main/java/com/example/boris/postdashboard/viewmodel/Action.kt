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

import com.example.boris.postdashboard.repository.Repository

/**
 * Action which signifies the action the app should take upon receiving an Intent
 */
sealed class Action {
    object LoadPostsAction : Action()
    data class ShowDetailViewAction(val selectedPostIndex: Int): Action()
    object ShowPostsWithoutLoading : Action()
    data class ShowOrHideComment(val commentVisible: Boolean) : Action()

    /**
     * Interprets [Action]s and returns a result of the action in the form of a [Result]. These actions are largely
     * performed Asynchronously using coroutines.
     */
    class ActionInterpreter(val repository: Repository) : Interpreter<Action, Result>() {

        override suspend fun interpret(input: Action, callback: suspend (Result) -> Unit) {
            callback(
                when(input) {
                    is LoadPostsAction -> {
                        callback(Result.PostsLoading)
                        postRequestResultToResult(repository.loadPosts())
                    }
                    is ShowDetailViewAction -> {
                        callback(Result.NavigateToDetails)
                        repository.loadPost(input.selectedPostIndex)
                    }
                    is ShowPostsWithoutLoading ->
                        postRequestResultToResult(repository.loadPosts())
                    is ShowOrHideComment -> {

                        // We load the last loaded post from the database, as the activity might have been destroyed
                        val lastLoadedPost = repository.lastLoadedPost()

                        when {
                            lastLoadedPost == null -> Result.PostsLoadingError("Last Loading Post is null")
                            input.commentVisible -> Result.HideComments(lastLoadedPost)
                            else -> Result.ShowComments(lastLoadedPost)
                        }
                    }
                }
            )
        }

        private suspend fun postRequestResultToResult(result: Repository.PostsRequestResult) : Result{
            return when (result)  {
                is Repository.PostsRequestResult.Success ->
                    Result.PostsLoadResult(result.posts, repository.lastLoadedPost())
                is Repository.PostsRequestResult.Error ->
                    Result.PostsLoadingError(result.message)
            }
        }

    }
}