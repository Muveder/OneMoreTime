package com.example.onemoretime.data

import android.content.Context

/**
 * Un contenedor de dependencias simple para proporcionar los repositorios a la app.
 */
interface AppContainer {
    val postRepository: PostRepository
    val usuarioRepository: UsuarioRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val postRepository: PostRepository by lazy {
        OfflinePostRepository(AppDatabase.getDatabase(context).postDao())
    }

    override val usuarioRepository: UsuarioRepository by lazy {
        OfflineUsuarioRepository(AppDatabase.getDatabase(context).usuarioDao())
    }
}
