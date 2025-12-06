package com.example.onemoretime.data

import com.example.onemoretime.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllPostsStream(): Flow<List<Post>>
    fun getTopRatedPostsStream(): Flow<List<Post>>
    fun getPostsByAuthorStream(author: String): Flow<List<Post>>
    fun getPostByIdStream(postId: Int): Flow<Post?>
    suspend fun createPost(post: Post)
    suspend fun updatePost(post: Post)
    fun searchPostsStream(query: String): Flow<List<Post>>
    suspend fun refreshPosts()
    suspend fun requestMorePosts(page: Int)
}

