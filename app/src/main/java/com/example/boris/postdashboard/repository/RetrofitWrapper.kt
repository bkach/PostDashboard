package com.example.boris.postdashboard.repository

import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class RetrofitWrapper {
    companion object {
        fun getJsonPlaceholderService(): JsonPlaceholderService = Retrofit.Builder()
            .baseUrl("http://jsonplaceholder.typicode.com")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(JsonPlaceholderService::class.java)
    }

    interface JsonPlaceholderService {
        @GET("/posts")
        fun getPosts(): Deferred<Response<List<Post>>>

        @GET("/users")
        fun getUsers(): Deferred<Response<List<User>>>

        @GET("/comments")
        fun getComments(): Deferred<Response<List<Comment>>>
    }
}

