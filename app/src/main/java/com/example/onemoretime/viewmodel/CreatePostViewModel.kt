package com.example.onemoretime.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.data.SessionManager
import com.example.onemoretime.model.Post
import kotlinx.coroutines.launch

class CreatePostViewModel(private val postRepository: PostRepository) : ViewModel() {

    var postUiState by mutableStateOf(PostUiState())
        private set

    fun updateUiState(newPostUiState: PostUiState) {
        postUiState = newPostUiState.copy()
    }

    fun savePost() {
        // Obtenemos el usuario actual del SessionManager
        val currentUser = SessionManager.currentUser.value
        if (validateInput() && currentUser != null) {
            viewModelScope.launch {
                // Le pasamos el nombre del autor al crear el Post
                postRepository.insertPost(postUiState.toPost(authorName = currentUser.nombre))
            }
        }
    }

    private fun validateInput(uiState: PostUiState = postUiState): Boolean {
        // La validación ya no necesita comprobar el autor
        return with(uiState) {
            title.isNotBlank() && community.isNotBlank()
        }
    }
}

/**
 * El estado de la UI ya no contiene al autor
 */
data class PostUiState(
    val id: Int = 0,
    val title: String = "",
    val community: String = "",
    val content: String = "",
    val rating: Float = 0.0f
)

/**
 * La función de extensión ahora requiere el nombre del autor
 */
fun PostUiState.toPost(authorName: String): Post = Post(
    id = id,
    title = title,
    community = community,
    author = authorName, // <-- ¡Aquí usamos el nombre del usuario logueado!
    content = content,
    rating = rating,
    timeAgo = "Just now", // Placeholder
    upvotes = 0,
    comments = 0
)
