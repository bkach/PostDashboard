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

package com.example.boris.postdashboard.repository

import com.example.boris.postdashboard.model.Comment
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.User
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Wrapper class for obtaining a Retrofit service
 */
class RetrofitWrapper {
    companion object {
        fun getJsonPlaceholderService(): JsonPlaceholderService = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildLoggingClient())
            .build().create(JsonPlaceholderService::class.java)

        private fun buildLoggingClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        }
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

