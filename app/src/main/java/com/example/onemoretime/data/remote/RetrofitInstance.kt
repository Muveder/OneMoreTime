package com.example.onemoretime.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // ¡¡¡MUY IMPORTANTE!!!
    // Esta es la dirección IP especial que usa el emulador de Android
    // para conectarse al 'localhost' (tu computadora).
    // NO uses 'localhost:8080' aquí.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Creamos una instancia de nuestro ApiService que podremos usar en toda la app.
    // Asegúrate de que importe el ApiService que creamos nosotros.
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}