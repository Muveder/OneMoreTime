package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.model.Post
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// Estado de la UI para la pantalla Home
data class HomeUiState(val posts: List<Post> = emptyList())

class HomeViewModel(postRepository: PostRepository) : ViewModel() {

    // El estado de la UI que la vista observará
    val uiState: StateFlow<HomeUiState> =
        postRepository.getAllPostsStream()
            .map { posts -> HomeUiState(posts = posts) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = HomeUiState()
            )
}
