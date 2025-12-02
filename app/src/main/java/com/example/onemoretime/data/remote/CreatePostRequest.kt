package com.example.onemoretime.data.remote

/**
 * DTO para enviar la información de un nuevo post al backend.
 * Solo contiene los campos que el backend necesita.
 */
data class CreatePostRequest(
    val title: String,
    val community: String,
    val author: String,
    val content: String?,
    val score: Int = 0,
    val comments: Int = 0,
    val rating: Float = 0.0f,
    val imageUrl: String?
)
    