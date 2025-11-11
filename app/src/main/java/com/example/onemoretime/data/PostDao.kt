package com.example.onemoretime.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.onemoretime.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    /**
     * Obtiene todos los posts de la base de datos, ordenados por ID descendente (más nuevos primero).
     */
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): Flow<List<Post>>

    /**
     * Obtiene todos los posts de la base de datos, ordenados por rating descendente (mejor valorados primero).
     */
    @Query("SELECT * FROM posts ORDER BY rating DESC")
    fun getTopRatedPosts(): Flow<List<Post>>

    /**
     * Obtiene todos los posts de un autor específico.
     */
    @Query("SELECT * FROM posts WHERE author = :author ORDER BY id DESC")
    fun getPostsByAuthor(author: String): Flow<List<Post>>

    /**
     * Inserta un nuevo post en la base de datos.
     * Si el post ya existe, lo reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)
}
