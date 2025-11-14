package com.example.onemoretime.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.data.SessionManager
import com.example.onemoretime.model.Post
import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreatePostViewModel(private val postRepository: PostRepository) : ViewModel() {

    var postUiState by mutableStateOf(PostUiState())
        private set

    // Estado para mantener al usuario actual de forma reactiva y segura
    private val _currentUser = MutableStateFlow<Usuario?>(null)

    init {
        viewModelScope.launch {
            SessionManager.currentUser.collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun updateUiState(newPostUiState: PostUiState) {
        postUiState = newPostUiState.copy()
    }

    fun onImageSelected(uri: Uri?) {
        postUiState = postUiState.copy(imageUrl = uri?.toString())
    }

    fun savePost(): Boolean {
        // Usamos el estado reactivo que siempre está actualizado
        val currentUser = _currentUser.value
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
    val rating: Float = 0.0f,
    val imageUrl: String? = null
)

fun PostUiState.toPost(authorName: String): Post = Post(
    id = id,
    title = title,
    community = community,
    author = authorName,
    content = content,
    rating = rating,
    timeAgo = "Just now",
    comments = 0,
    score = 0,
    imageUrl = imageUrl
)
