package com.example.onemoretime.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.model.Post
import kotlinx.coroutines.launch

class CreatePostViewModel(private val postRepository: PostRepository) : ViewModel() {

    /**
     * El estado de la UI para la pantalla de creación de post.
     */
    var postUiState by mutableStateOf(PostUiState())
        private set

    /**
     * Actualiza el estado de la UI con los nuevos valores introducidos por el usuario.
     */
    fun updateUiState(newPostUiState: PostUiState) {
        postUiState = newPostUiState.copy()
    }

    /**
     * Valida y guarda el post en la base de datos.
     */
    suspend fun savePost(): Boolean {
        if (!validateInput()) {
            return false
        }
        postRepository.insertPost(postUiState.toPost())
        return true
    }

    private fun validateInput(uiState: PostUiState = postUiState): Boolean {
        return with(uiState) {
            title.isNotBlank() && community.isNotBlank() && author.isNotBlank()
        }
    }
}

/**
 * Representa el estado de la UI para un Post mientras se está creando.
 */
data class PostUiState(
    val id: Int = 0,
    val title: String = "",
    val community: String = "",
    val author: String = "Erica", // Placeholder, a futuro se tomará del usuario logueado
    val content: String = "",
    val rating: Float = 0.0f
)

/**
 * Función de extensión para convertir un [PostUiState] en un [Post] que se puede guardar en la DB.
 */
fun PostUiState.toPost(): Post = Post(
    id = id,
    title = title,
    community = community,
    author = author,
    content = content,
    rating = rating,
    timeAgo = "Just now", // Placeholder
    upvotes = 0,
    comments = 0
)
