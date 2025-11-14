package com.example.onemoretime.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val community: String,
    val author: String,
    val content: String?,
    val rating: Float,
    val timeAgo: String,
    val comments: Int,
    val score: Int,
    val imageUrl: String? = null // <-- Nuevo campo para la imagen
)
