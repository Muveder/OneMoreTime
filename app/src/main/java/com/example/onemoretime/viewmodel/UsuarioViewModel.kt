package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.SessionManager
import com.example.onemoretime.data.UsuarioRepository
import com.example.onemoretime.model.Usuario
import com.example.onemoretime.model.UsuarioErrores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores(),
    val registroExitoso: Boolean = false
)

class UsuarioViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, errores = it.errores.copy(nombre = null, registro = null)) }
    }

    fun onCorreoChange(correo: String) {
        _uiState.update { it.copy(correo = correo, errores = it.errores.copy(correo = null, registro = null)) }
    }

    fun onClaveChange(clave: String) {
        _uiState.update { it.copy(clave = clave, errores = it.errores.copy(clave = null, registro = null)) }
    }

    fun onDireccionChange(direccion: String) {
        _uiState.update { it.copy(direccion = direccion, errores = it.errores.copy(direccion = null, registro = null)) }
    }

    fun onAceptaTerminosChange(acepta: Boolean) {
        _uiState.update { it.copy(aceptaTerminos = acepta) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _uiState.value
        var exito = true

        val nuevosErrores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "El nombre es obligatorio" else null,
            correo = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(estadoActual.correo).matches()) "Correo inválido" else null,
            clave = if (estadoActual.clave.length < 6) "Mínimo 6 caracteres" else null,
            direccion = if (estadoActual.direccion.isBlank()) "La dirección es obligatoria" else null
        )

        if (nuevosErrores.nombre != null || nuevosErrores.correo != null || nuevosErrores.clave != null || nuevosErrores.direccion != null) {
            exito = false
        }

        _uiState.update { it.copy(errores = nuevosErrores) }

        return exito && estadoActual.aceptaTerminos
    }

    fun guardarUsuario() {
        if (validarFormulario()) {
            viewModelScope.launch {
                val estadoActual = _uiState.value
                val nuevoUsuario = Usuario(
                    nombre = estadoActual.nombre,
                    correo = estadoActual.correo,
                    clave = estadoActual.clave, // Recuerda encriptar esto en una app real
                    direccion = estadoActual.direccion
                )
                
                val resultadoId = usuarioRepository.insertUsuario(nuevoUsuario)
                
                if (resultadoId == -1L) {
                    _uiState.update {
                        it.copy(errores = it.errores.copy(registro = "El nombre o el correo ya están en uso."))
                    }
                } else {
                    val usuarioLogueado = usuarioRepository.getUsuarioPorNombreStream(nuevoUsuario.nombre).first()
                    if (usuarioLogueado != null) {
                        SessionManager.login(usuarioLogueado)
                        _uiState.update { it.copy(registroExitoso = true) }
                    }
                }
            }
        }
    }
}
