package com.example.boris.postdashboard.repository

import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User
import com.example.boris.postdashboard.repository.RetrofitWrapper.JsonPlaceholderService
import com.example.boris.postdashboard.viewmodel.Result
import com.example.boris.postdashboard.viewmodel.Result.LoadPostsResult
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import kotlin.coroutines.CoroutineContext

class Repository : CoroutineScope, KoinComponent {

    val service: JsonPlaceholderService by inject()
    var posts: List<Post>? = null
    var comments: List<Comment>? = null
    var users: List<User>? = null

    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun loadPosts(): Deferred<Result> = async {
        delay(1000)

        if (posts != null) {
            System.out.println("Bdebug -> Cached posts")
            LoadPostsResult(posts!!)
        } else {
            System.out.println("Bdebug -> REQUEST FOR POSTS")
            val postsResponse = service.getPosts().await()

            if (postsResponse.isSuccessful && postsResponse.body() != null) {
                posts = postsResponse.body()!!
                LoadPostsResult(posts!!)
            } else {
                Result.PostLoadingError
            }
        }
    }

    fun loadDetails(selectedPost: Post)  = async {
        delay(1000)

        if (users != null && comments != null) {
            System.out.println("Bdebug -> Cached users and comments")
            Result.LoadDetailsResult(updatePost(selectedPost))
        } else {
            System.out.println("Bdebug -> REQUEST FOR USERS AND COMMENTS")
            val usersRequest = service.getUsers().await()
            val commentsRequest = service.getComments().await()

            if (usersRequest.isSuccessful && commentsRequest.isSuccessful &&
                usersRequest.body() != null && commentsRequest.body() != null) {
                users = usersRequest.body()!!
                comments = commentsRequest.body()!!
                Result.LoadDetailsResult(updatePost(selectedPost))
            } else {
                Result.DetailsLoadingError
            }
        }
    }

    fun updatePost(post: Post): Post {
        val user = users!!.find { it.id == post.userId }
        val numComments = comments!!.filter { it.postId == post.id }.size

        post.userName = user?.name
        post.numComments = numComments

        return post
    }

}