package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.data.SessionManager
import com.example.onemoretime.data.UsuarioRepository
import com.example.onemoretime.model.Post
import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn


data class ProfileUiState(
    val usuario: Usuario? = null,
    val posts: List<Post> = emptyList()
)

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    usuarioRepository: UsuarioRepository,
    postRepository: PostRepository
) : ViewModel() {

    // Este ViewModel ahora reacciona a los cambios en el SessionManager
    val uiState: StateFlow<ProfileUiState> = SessionManager.currentUser.flatMapLatest { currentUser ->
        if (currentUser == null) {
            // Si no hay nadie logueado, emitimos un estado vacío
            flowOf(ProfileUiState())
        } else {
            // Si hay un usuario, combinamos la información de su perfil y sus posts
            combine(
                usuarioRepository.getUsuarioPorNombreStream(currentUser.nombre),
                postRepository.getPostsByAuthorStream(currentUser.nombre)
            ) { usuario, posts ->
                ProfileUiState(usuario = usuario, posts = posts)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ProfileUiState()
    )
}
