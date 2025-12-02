package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la UI para la pantalla Home, ahora con paginación.
 */
data class HomeUiState(
    val posts: List<Post> = emptyList(),
    val currentPage: Int = 1, // La siguiente página a cargar
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false
)

class HomeViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Observa la base de datos y actualiza la lista de posts en el estado de la UI
        postRepository.getAllPostsStream()
            .onEach { posts ->
                _uiState.update { it.copy(posts = posts) }
            }
            .launchIn(viewModelScope)

        // Carga inicial de datos desde la red
        refreshPosts()
    }

    /**
     * Carga la primera página de posts, limpiando los datos antiguos.
     */
    fun refreshPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, currentPage = 1) }
            postRepository.refreshPosts() // Llama a la API (página 0) y limpia la BD
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    /**
     * Carga la siguiente página de posts y los añade a la lista.
     */
    fun loadMorePosts() {
        if (_uiState.value.isLoadingMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            postRepository.requestMorePosts(page = _uiState.value.currentPage)
            _uiState.update { it.copy(
                currentPage = it.currentPage + 1,
                isLoadingMore = false
            ) }
        }
    }
}
