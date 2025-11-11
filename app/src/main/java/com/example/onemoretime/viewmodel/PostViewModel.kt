package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.model.Post
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// Estado de la UI para la pantalla de la lista de posts
data class PostListUiState(val postList: List<Post> = emptyList())

class PostViewModel(postRepository: PostRepository) : ViewModel() {

    /**
     * El estado de la UI que la vista observará.
     */
    val uiState: StateFlow<PostListUiState> =
        postRepository.getAllPostsStream()
            .map { PostListUiState(it) } // Mapea la lista de posts al objeto de estado de la UI
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PostListUiState()
            )

    // Las funciones para añadir/votar posts se implementarán aquí en el futuro,
    // usando viewModelScope.launch para llamar a las funciones suspend del repositorio.
}
