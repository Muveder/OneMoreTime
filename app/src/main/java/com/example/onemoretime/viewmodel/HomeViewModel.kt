package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModel
import com.example.onemoretime.model.Post
import com.example.onemoretime.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val repository = PostRepository()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        _posts.value = repository.getPosts()
    }
}
