package com.example.onemoretime.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.SessionManager
import com.example.onemoretime.data.UsuarioRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    // Renombramos 'emailInput' a 'credentialInput' para que sea más genérico
    var credentialInput by mutableStateOf("")
        private set
    var passwordInput by mutableStateOf("")
        private set
    var loginError by mutableStateOf<String?>(null)
        private set
    var loginSuccess by mutableStateOf(false)
        private set

    fun onCredentialChange(credential: String) {
        credentialInput = credential
        loginError = null
    }

    fun onPasswordChange(password: String) {
        passwordInput = password
        loginError = null
    }

    fun login() {
        viewModelScope.launch {
            // Usamos la nueva función de búsqueda flexible
            val usuario = usuarioRepository.findUserByCredential(credentialInput)

            if (usuario != null && usuario.clave == passwordInput) {
                SessionManager.login(usuario)
                loginSuccess = true
            } else {
                loginError = "Credenciales incorrectas o usuario no encontrado"
            }
        }
    }
}
