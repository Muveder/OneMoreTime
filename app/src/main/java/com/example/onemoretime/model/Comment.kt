package com.example.onemoretime.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = Post::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE // Si se borra un post, se borran sus comentarios
        )
    ]
)
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postId: Int, // El ID del post al que pertenece
    val parentId: Int? = null, // El ID del comentario padre (si es una respuesta)
    val author: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val score: Int = 0
)
