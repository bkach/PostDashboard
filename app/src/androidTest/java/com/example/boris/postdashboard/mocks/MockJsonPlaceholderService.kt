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

package com.example.boris.postdashboard.mocks

import com.example.boris.postdashboard.mocks.MockInstrumentationModel.Companion.mockComment
import com.example.boris.postdashboard.mocks.MockInstrumentationModel.Companion.mockPhoto
import com.example.boris.postdashboard.mocks.MockInstrumentationModel.Companion.mockPost
import com.example.boris.postdashboard.mocks.MockInstrumentationModel.Companion.mockUser
import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Photo
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User
import com.example.boris.postdashboard.repository.RetrofitWrapper
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response

class MockJsonPlaceholderService : RetrofitWrapper.JsonPlaceholderService {

    var getPostsSuccess = true
    var getCommentsSuccess = true
    var getUsersSuccess = true
    var getPhotosSuccess = true

    override fun getPosts(): Deferred<Response<List<Post>>> = buildMockResponse(getPostsSuccess, mockPost)

    override fun getUsers(): Deferred<Response<List<User>>> = buildMockResponse(getUsersSuccess, mockUser)

    override fun getComments(): Deferred<Response<List<Comment>>> = buildMockResponse(getCommentsSuccess, mockComment)

    override fun getPhotos(): Deferred<Response<List<Photo>>> = buildMockResponse(getPhotosSuccess, mockPhoto)

    private fun <T> buildMockResponse(shouldSucceed: Boolean, data: T): Deferred<Response<List<T>>> {
        return if (shouldSucceed) {
            CompletableDeferred(Response.success(listOf(data)))
        } else {
            CompletableDeferred(
                Response.error(404, ResponseBody.create(null, "error"))
            )
        }
    }
}
