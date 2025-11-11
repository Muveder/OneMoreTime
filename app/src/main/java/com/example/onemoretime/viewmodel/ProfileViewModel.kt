package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(postRepository: PostRepository) : ViewModel() {

    // Placeholder para el nombre del usuario. A futuro, esto vendría del login.
    private val currentAuthor = "Erica"

    /**
     * El estado de la UI que la vista observará. Contiene los posts del autor actual.
     */
    val uiState: StateFlow<PostListUiState> =
        postRepository.getPostsByAuthorStream(currentAuthor) // <-- ¡Aquí usamos la nueva función!
            .map { PostListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PostListUiState()
            )

    // Podríamos añadir más detalles del perfil aquí, como el karma, fecha de registro, etc.
}
