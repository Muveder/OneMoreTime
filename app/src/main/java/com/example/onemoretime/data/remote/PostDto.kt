package com.example.onemoretime.data.remote

import com.google.gson.annotations.SerializedName

// Este es el objeto que representa el JSON que viene de la API
data class PostDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("community")
    val community: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String?,
    @SerializedName("score")
    val score: Int,
    @SerializedName("comments")
    val comments: Int,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("createdAt")
    val createdAt: String // Retrofit lo leerá como String, ya lo convertiremos
)
    