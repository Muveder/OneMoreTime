package com.example.onemoretime.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.onemoretime.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts ORDER BY rating DESC")
    fun getTopRatedPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE author = :author ORDER BY id DESC")
    fun getPostsByAuthor(author: String): Flow<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: Int): Flow<Post>

    // Nueva función para actualizar un post (para las reacciones)
    @Update
    suspend fun updatePost(post: Post)
}
