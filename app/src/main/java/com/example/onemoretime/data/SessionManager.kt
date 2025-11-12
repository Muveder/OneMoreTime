package com.example.onemoretime.data

import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Un objeto singleton para gestionar el estado de la sesión del usuario en toda la aplicación.
 */
object SessionManager {

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

    /**
     * Inicia la sesión de un usuario.
     */
    fun login(usuario: Usuario) {
        _currentUser.value = usuario
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    fun logout() {
        _currentUser.value = null
    }
}
