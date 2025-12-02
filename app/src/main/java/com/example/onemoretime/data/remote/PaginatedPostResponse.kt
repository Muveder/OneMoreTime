package com.example.onemoretime.data.remote

import com.google.gson.annotations.SerializedName

/**
 * DTO que representa la respuesta paginada de nuestro propio backend.
 */
data class PaginatedPostResponse(
    @SerializedName("content")val content: List<PostDto>
)
    