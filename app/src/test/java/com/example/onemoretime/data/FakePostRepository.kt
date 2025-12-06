package com.example.onemoretime.data

import com.example.onemoretime.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake repository para pruebas unitarias.
 */
class FakePostRepository : PostRepository {

    // StateFlow con valor inicial vacío, así el observer recibe siempre el último estado
    private val postsFlow = MutableStateFlow<List<Post>>(emptyList())
    private val samplePosts = mutableListOf<Post>()

    override fun getAllPostsStream(): Flow<List<Post>> {
        return postsFlow
    }

    // Implementaciones mínimas para cumplir la interfaz
    override fun getTopRatedPostsStream(): Flow<List<Post>> = flowOf(emptyList())
    override fun getPostsByAuthorStream(author: String): Flow<List<Post>> = flowOf(emptyList())
    override fun getPostByIdStream(postId: Int): Flow<Post?> = flowOf(null)
    override suspend fun updatePost(post: Post) {}
    override fun searchPostsStream(query: String): Flow<List<Post>> = flowOf(emptyList())

    override suspend fun createPost(post: Post) {
        samplePosts.add(post)
        postsFlow.value = samplePosts.toList()
    }

    override suspend fun refreshPosts() {
        val networkPosts = listOf(
            Post(id = 100, title = "Post de Test 1", community = "testing", author = "tester", timeAgo = "now", content = "", rating = 4f, comments = 0, score = 0),
            Post(id = 101, title = "Post de Test 2", community = "testing", author = "tester", timeAgo = "now", content = "", rating = 5f, comments = 0, score = 0)
        )
        samplePosts.clear()
        samplePosts.addAll(networkPosts)
        postsFlow.value = samplePosts.toList()
    }

    override suspend fun requestMorePosts(page: Int) {
        val morePosts = listOf(
            Post(id = 102 + page, title = "Post Adicional ${page + 1}", community = "testing", author = "tester", timeAgo = "now", content = "", rating = 3f, comments = 0, score = 0)
        )
        samplePosts.addAll(morePosts)
        postsFlow.value = samplePosts.toList()
    }
}
