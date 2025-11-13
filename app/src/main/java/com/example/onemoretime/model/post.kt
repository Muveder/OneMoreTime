package com.example.onemoretime.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val author: String,
    val community: String,
    val timeAgo: String,
    val title: String,
    val content: String? = null,
    val rating: Float,
    val comments: Int,
    // Nuevo sistema de votación
    val score: Int = 0
)
