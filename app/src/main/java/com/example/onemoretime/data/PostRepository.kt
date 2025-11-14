package com.example.onemoretime.data

import com.example.onemoretime.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para el repositorio de Posts.
 */
interface PostRepository {
    fun getAllPostsStream(): Flow<List<Post>>
    fun getTopRatedPostsStream(): Flow<List<Post>>
    fun getPostsByAuthorStream(author: String): Flow<List<Post>>
    // CORREGIDO: Ahora puede devolver un Flow<Post?> para manejar posts no encontrados
    fun getPostByIdStream(postId: Int): Flow<Post?>
    suspend fun insertPost(post: Post)
    suspend fun updatePost(post: Post)
    fun searchPostsStream(query: String): Flow<List<Post>>
}

/**
 * Implementación del repositorio que usa el DAO para interactuar con la base de datos local.
 */
class OfflinePostRepository(private val postDao: PostDao) : PostRepository {
    override fun getAllPostsStream(): Flow<List<Post>> = postDao.getAllPosts()

    override fun getTopRatedPostsStream(): Flow<List<Post>> = postDao.getTopRatedPosts()

    override fun getPostsByAuthorStream(author: String): Flow<List<Post>> = postDao.getPostsByAuthor(author)

    // CORREGIDO: Implementación actualizada para que coincida con la interfaz
    override fun getPostByIdStream(postId: Int): Flow<Post?> = postDao.getPostById(postId)

    override suspend fun insertPost(post: Post) {
        postDao.insertPost(post)
    }

    override suspend fun updatePost(post: Post) {
        postDao.updatePost(post)
    }

    override fun searchPostsStream(query: String): Flow<List<Post>> = postDao.searchPosts(query)
}
