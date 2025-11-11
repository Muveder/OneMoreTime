package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import com.example.onemoretime.model.UsuarioErrores
import com.example.onemoretime.model.UsuarioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUIState())
    val uiState: StateFlow<UsuarioUIState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update {
            // Cuando el usuario escribe, limpiamos el error de ese campo
            it.copy(nombre = username, errores = it.errores.copy(nombre = null))
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            // Cuando el usuario escribe, limpiamos el error de ese campo
            it.copy(clave = password, errores = it.errores.copy(clave = null))
        }
    }

    fun login(): Boolean {
        val currentState = _uiState.value
        var exito = true

        // Validaciones
        val nuevosErrores = UsuarioErrores(
            nombre = if (currentState.nombre.isBlank()) "El usuario es obligatorio" else null,
            clave = if (currentState.clave.isBlank()) "La contraseña es obligatoria" else null
        )

        if (nuevosErrores.nombre != null || nuevosErrores.clave != null) {
            exito = false
        }

        // Actualizamos el estado con los errores (si los hay)
        _uiState.update {
            it.copy(errores = nuevosErrores)
        }

        return exito
    }
}
