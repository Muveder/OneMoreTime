package com.example.onemoretime.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.UsuarioRepository
import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Define los posibles estados de la sesión de usuario.
 */
sealed interface SessionUiState {
    object NotLoggedIn : SessionUiState // El usuario no ha iniciado sesión
    data class LoggedIn(val usuario: Usuario) : SessionUiState // El usuario ha iniciado sesión
}

class LoginViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    /**
     * Mantiene el estado de la sesión actual (logueado o no).
     * Es un StateFlow para que otras partes de la app puedan observarlo.
     */
    private val _sessionState = MutableStateFlow<SessionUiState>(SessionUiState.NotLoggedIn)
    val sessionState: StateFlow<SessionUiState> = _sessionState.asStateFlow()

    // Estado para los campos de texto del formulario de login
    var emailInput by mutableStateOf("")
        private set
    var passwordInput by mutableStateOf("")
        private set
    var loginError by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(email: String) {
        emailInput = email
        loginError = null
    }

    fun onPasswordChange(password: String) {
        passwordInput = password
        loginError = null
    }

    /**
     * Intenta iniciar sesión validando las credenciales contra la base de datos.
     */
    fun login() {
        viewModelScope.launch {
            // Esta es una implementación muy básica de login. ¡No la uses en producción!
            // En una app real, la validación se haría contra un servidor y las contraseñas estarían encriptadas.
            try {
                val usuario = usuarioRepository.getUsuarioPorCorreo(emailInput)
                if (usuario != null && usuario.clave == passwordInput) {
                    _sessionState.value = SessionUiState.LoggedIn(usuario)
                } else {
                    loginError = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                loginError = "El usuario no existe"
            }
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    fun logout() {
        _sessionState.value = SessionUiState.NotLoggedIn
    }
}
