package com.example.onemoretime.data.remote

import com.example.onemoretime.data.remote.GameApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz de Retrofit específica para la API de RAWG.
 */
interface RawgApiService {

    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("page") page: Int // <-- NUEVO: Para solicitar una página específica
    ): GameApiResponse
}
