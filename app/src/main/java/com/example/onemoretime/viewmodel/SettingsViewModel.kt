package com.example.onemoretime.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.SessionManager
import com.example.onemoretime.data.UsuarioRepository
import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class SettingsUiState(
    val nombreReal: String = "",
    val cumpleanos: String = "",
    val avatarUrl: String? = null, // <-- Nuevo campo para el avatar
    val isLoading: Boolean = true,
    val saveSuccess: Boolean = false
)

class SettingsViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    var uiState by mutableStateOf(SettingsUiState())
        private set

    private var currentUser: Usuario? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val user = SessionManager.currentUser.first()
            if (user != null) {
                currentUser = user
                uiState = uiState.copy(
                    nombreReal = user.nombreReal ?: "",
                    cumpleanos = user.cumpleanos?.let { dateFormat.format(Date(it)) } ?: "",
                    avatarUrl = user.avatarUrl, // <-- Cargamos el avatar actual
                    isLoading = false
                )
            }
        }
    }

    fun onNombreRealChange(nombre: String) {
        uiState = uiState.copy(nombreReal = nombre)
    }

    fun onCumpleanosChange(fecha: String) {
        uiState = uiState.copy(cumpleanos = fecha)
    }

    // Nueva función para cuando el usuario selecciona un nuevo avatar
    fun onAvatarChange(uri: Uri) {
        uiState = uiState.copy(avatarUrl = uri.toString())
    }

    fun saveSettings() {
        val userToUpdate = currentUser
        if (userToUpdate != null) {
            viewModelScope.launch {
                val cumpleanosTimestamp = try {
                    dateFormat.parse(uiState.cumpleanos)?.time
                } catch (e: Exception) {
                    null
                }

                val updatedUser = userToUpdate.copy(
                    nombreReal = uiState.nombreReal,
                    cumpleanos = cumpleanosTimestamp,
                    avatarUrl = uiState.avatarUrl // <-- Guardamos el nuevo avatar
                )
                usuarioRepository.updateUsuario(updatedUser)
                SessionManager.login(updatedUser) // Actualizamos la sesión para reflejar los cambios
                uiState = uiState.copy(saveSuccess = true)
            }
        }
    }
}
