package com.example.onemoretime.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.model.Post
import com.example.onemoretime.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreatePostViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    fun updateUiState(newPostUiState: PostUiState) {
        _uiState.update { newPostUiState.copy() }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(imageUrl = uri?.toString()) }
    }

    // ARREGLO DEFINITIVO: La función ahora recibe TODO lo que necesita.
    // No depende de estados internos ocultos. Es pura y predecible.
    suspend fun savePost(uiState: PostUiState, currentUser: Usuario?): Boolean {
        if (!validateInput(uiState) || currentUser == null) {
            return false
        }

        postRepository.createPost(uiState.toPost(authorName = currentUser.nombre))
        return true
    }

    // La validación también es ahora una función pura que recibe el estado.
    private fun validateInput(uiState: PostUiState): Boolean {
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
