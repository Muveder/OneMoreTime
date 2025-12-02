package com.example.onemoretime.data

import androidx.room.Transaction
import com.example.onemoretime.data.remote.ApiService
import com.example.onemoretime.data.remote.CreatePostRequest
import com.example.onemoretime.data.remote.PostDto
import com.example.onemoretime.model.Post
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

interface PostRepository {
    fun getAllPostsStream(): Flow<List<Post>>
    fun getTopRatedPostsStream(): Flow<List<Post>>
    fun getPostsByAuthorStream(author: String): Flow<List<Post>>
    fun getPostByIdStream(postId: Int): Flow<Post?>
    suspend fun createPost(post: Post) // <-- Renombrado para claridad
    suspend fun updatePost(post: Post)
    fun searchPostsStream(query: String): Flow<List<Post>>
    suspend fun refreshPosts()
    suspend fun requestMorePosts(page: Int)
}

class DefaultPostRepository(
    private val postDao: PostDao,
    private val apiService: ApiService
) : PostRepository {

    override fun getAllPostsStream(): Flow<List<Post>> = postDao.getAllPosts()
    override fun getTopRatedPostsStream(): Flow<List<Post>> = postDao.getTopRatedPosts()
    override fun getPostsByAuthorStream(author: String): Flow<List<Post>> = postDao.getPostsByAuthor(author)
    override fun getPostByIdStream(postId: Int): Flow<Post?> = postDao.getPostById(postId)
    override suspend fun updatePost(post: Post) { postDao.updatePost(post) } // Se mantiene local por ahora
    override fun searchPostsStream(query: String): Flow<List<Post>> = postDao.searchPosts(query)

    override suspend fun createPost(post: Post) {
        try {
            val request = CreatePostRequest(
                title = post.title,
                community = post.community,
                author = post.author,
                content = post.content,
                imageUrl = post.imageUrl
                // El resto de campos los inicializa el backend
            )
            apiService.createPost(request)
            // Después de crear, refrescamos para obtener la lista actualizada
            refreshPosts()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Transaction
    override suspend fun refreshPosts() {
        try {
            val response = apiService.getAllPosts(page = 0)
            val postsToInsert = response.content.map { it.toEntity() }
            postDao.deleteAll()
            postDao.insertAll(postsToInsert)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun requestMorePosts(page: Int) {
        try {
            val response = apiService.getAllPosts(page = page)
            val postsToInsert = response.content.map { it.toEntity() }
            postDao.insertAll(postsToInsert)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

// Funciones de mapeo (sin cambios)
private fun PostDto.toEntity(): Post {
    return Post(
        id = this.id.toInt(),
        title = this.title,
        community = this.community,
        author = this.author,
        content = this.content,
        rating = this.rating,
        timeAgo = this.createdAt.toFormattedString(),
        comments = this.comments,
        score = this.score,
        imageUrl = this.imageUrl
    )
}

private fun String.toFormattedString(): String {
    return try {
        val instant = Instant.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy").withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        "-"
    }
}
