package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class ExploreTab { NEW, TOP }

// El estado ahora incluye ambas listas y la pestaña seleccionada
data class ExploreUiState(
    val newPosts: List<Post> = emptyList(),
    val topRatedPosts: List<Post> = emptyList(),
    val selectedTab: ExploreTab = ExploreTab.TOP
)

class ExploreViewModel(postRepository: PostRepository) : ViewModel() {

    private val _selectedTab = MutableStateFlow(ExploreTab.TOP)

    // El estado de la UI combina ambas listas y la pestaña seleccionada
    val uiState: StateFlow<ExploreUiState> = combine(
        postRepository.getAllPostsStream(), // Para la pestaña "Nuevos"
        postRepository.getTopRatedPostsStream(), // Para la pestaña "Top"
        _selectedTab
    ) { newPosts, topPosts, tab ->
        ExploreUiState(
            newPosts = newPosts,
            topRatedPosts = topPosts,
            selectedTab = tab
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ExploreUiState()
    )

    /**
     * Se llama cuando el usuario pulsa una de las pestañas.
     */
    fun selectTab(tab: ExploreTab) {
        _selectedTab.value = tab
    }
}
