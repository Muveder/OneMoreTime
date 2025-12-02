package com.example.onemoretime.data.remote // <-- Paquete corregido

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta completa de la API de juegos de RAWG.
 */
data class GameApiResponse(
    @SerializedName
        ("results")
    val results: List<GameDto>
)

/**
 * DTO que representa un único juego de la API de RAWG.
 */
data class GameDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("background_image")
    val backgroundImage: String,

    @SerializedName("rating")
    val rating: Float
)