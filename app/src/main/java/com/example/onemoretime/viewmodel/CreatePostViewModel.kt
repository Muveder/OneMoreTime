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

    fun savePost(): Boolean {
        val currentUser = SessionManager.currentUser.value
        if (!validateInput() || currentUser == null) {
            return false
        }

        viewModelScope.launch {
            postRepository.insertPost(postUiState.toPost(authorName = currentUser.nombre))
        }
        return true
    }

    private fun validateInput(uiState: PostUiState = postUiState): Boolean {
        return with(uiState) {
            title.isNotBlank() && community.isNotBlank()
        }
    }
}

data class PostUiState(
    val id: Int = 0,
    val title: String = "",
    val community: String = "",
    val content: String = "",
    val rating: Float = 0.0f
)

// Modificado para usar el nuevo campo 'score' en lugar de los contadores de emojis
fun PostUiState.toPost(authorName: String): Post = Post(
    id = id,
    title = title,
    community = community,
    author = authorName,
    content = content,
    rating = rating,
    timeAgo = "Just now",
    comments = 0,
    score = 0 // Los posts nuevos empiezan con una puntuación de 0
)
