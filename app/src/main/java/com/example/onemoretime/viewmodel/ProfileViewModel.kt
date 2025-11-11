package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.data.UsuarioRepository
import com.example.onemoretime.model.Post
import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// Nuevo estado de la UI para la pantalla de perfil
data class ProfileUiState(
    val usuario: Usuario? = null,
    val posts: List<Post> = emptyList()
)

class ProfileViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    // Placeholder para el nombre del usuario. A futuro, esto vendría del login.
    private val currentAuthor = "Erica"

    // Combina el Flow del usuario y el Flow de sus posts en un único estado de UI.
    val uiState: StateFlow<ProfileUiState> = combine(
        usuarioRepository.getUsuarioPorNombreStream(currentAuthor),
        postRepository.getPostsByAuthorStream(currentAuthor)
    ) { usuario, posts ->
        ProfileUiState(usuario = usuario, posts = posts)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ProfileUiState()
    )
}
