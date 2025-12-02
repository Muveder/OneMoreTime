package com.example.onemoretime.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Instancia de Retrofit dedicada exclusivamente a la API de RAWG.
 */
object RawgRetrofitInstance {

    private const val BASE_URL = "https://api.rawg.io/api/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Instancia del servicio de la API de RAWG para ser usada en la app.
     */
    val api: RawgApiService by lazy {
        retrofit.create(RawgApiService::class.java)
    }
}