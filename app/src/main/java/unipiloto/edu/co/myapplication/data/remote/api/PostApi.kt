package com.unipiloto.myapplication.data.remote.api

import com.unipiloto.myapplication.data.remote.dto.PostDto
import retrofit2.Response
import retrofit2.http.GET

interface PostApi {
    @GET("posts")
    suspend fun getPosts(): Response<List<PostDto>>

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}