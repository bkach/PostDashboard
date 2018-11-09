package com.example.boris.postdashboard

import com.example.boris.postdashboard.model.Comment
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

    val mockPost = Post(1, 1, "Title", "Body", null, null)
    private val mockUser = User(1, "hello")
    private val mockComment = Comment(1, 1)

    override fun getPosts(): Deferred<Response<List<Post>>> = buildMockResponse(getPostsSuccess, mockPost)

    override fun getUsers(): Deferred<Response<List<User>>> = buildMockResponse(getUsersSuccess, mockUser)

    override fun getComments(): Deferred<Response<List<Comment>>> = buildMockResponse(getCommentsSuccess, mockComment)

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
