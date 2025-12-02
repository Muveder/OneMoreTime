package com.example.onemoretime.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interfaz de Retrofit para nuestro propio backend.
 */
interface ApiService {

    @GET("api/posts")
    suspend fun getAllPosts(
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PaginatedPostResponse

    // NUEVO: Endpoint para crear un nuevo post
    @POST("api/posts")
    suspend fun createPost(@Body postRequest: CreatePostRequest): PostDto
}
    