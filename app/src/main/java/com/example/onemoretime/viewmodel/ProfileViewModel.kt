package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.data.SessionManager
import com.example.onemoretime.data.UsuarioRepository
import com.example.onemoretime.model.Post
import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ProfileUiState(
    val usuario: Usuario? = null,
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = true // Empezamos en estado de carga
)

class ProfileViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Escuchamos los cambios en la sesión del usuario
            SessionManager.currentUser.collect { currentUser ->
                if (currentUser != null) {
                    // Cuando hay un usuario, combinamos sus datos y sus posts
                    combine(
                        usuarioRepository.getUsuarioPorNombreStream(currentUser.nombre),
                        postRepository.getPostsByAuthorStream(currentUser.nombre)
                    ) { usuario, posts ->
                        // En cuanto tengamos ambos, actualizamos el estado
                        _uiState.value = ProfileUiState(usuario = usuario, posts = posts, isLoading = false)
                    }.collect() // El .collect() es crucial para que el combine se active
                } else {
                    // Si no hay nadie logueado, reseteamos el estado a uno no-cargando
                    _uiState.value = ProfileUiState(isLoading = false)
                }
            }
        }
    }
}
