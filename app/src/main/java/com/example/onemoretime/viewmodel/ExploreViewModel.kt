package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ExploreViewModel(postRepository: PostRepository) : ViewModel() {

    /**
     * El estado de la UI que la vista observará. Contiene la lista de los posts mejor valorados.
     */
    val uiState: StateFlow<PostListUiState> =
        postRepository.getTopRatedPostsStream() // <-- ¡Aquí usamos la nueva función!
            .map { PostListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PostListUiState()
            )
}
