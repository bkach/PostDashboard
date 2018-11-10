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
 * An class which describes an Intent from the UI
 */
sealed class Intent {
    object LoadPostData : Intent()
    object LeaveDetailIntent : Intent()
    data class CommentTapped(val commentVisible: Boolean) : Intent()
    data class SelectPostIntent(val selectedPost: PostWithMetadata) : Intent()

    /**
     * Maps [Intent]s to [Action]s
     */
    class IntentInterpreter : Interpreter<Intent, Action>() {
        override suspend fun interpret(input: Intent, callback: suspend (Action) -> Unit) {
            callback(
                when (input) {
                    is LoadPostData -> Action.LoadPostsAction
                    is SelectPostIntent -> Action.ShowDetailViewAction(input.selectedPost)
                    LeaveDetailIntent -> Action.ShowPostsWithoutLoading
                    is CommentTapped -> Action.ShowOrHideComment(input.commentVisible)
                }
            )
        }
    }
}
