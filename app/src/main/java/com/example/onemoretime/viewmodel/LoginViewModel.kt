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

    // Estado para los campos de texto y errores
    var emailInput by mutableStateOf("")
        private set
    var passwordInput by mutableStateOf("")
        private set
    var loginError by mutableStateOf<String?>(null)
        private set
    var loginSuccess by mutableStateOf(false)
        private set

    fun onEmailChange(email: String) {
        emailInput = email
        loginError = null
    }

    fun onPasswordChange(password: String) {
        passwordInput = password
        loginError = null
    }

    fun login() {
        viewModelScope.launch {
            // 1. Buscamos al usuario por su correo electrónico. Esto devuelve un Usuario? (puede ser null)
            val usuario = usuarioRepository.getUsuarioPorCorreo(emailInput)

            // 2. Comprobamos si el usuario existe Y si la contraseña de ESE usuario coincide
            if (usuario != null && usuario.clave == passwordInput) {
                // Si el login es correcto, informamos al SessionManager
                SessionManager.login(usuario)
                loginSuccess = true
            } else {
                // Si el usuario es null o la contraseña no coincide, mostramos un error
                loginError = "Credenciales incorrectas o usuario no encontrado"
            }
        }
    }
}
