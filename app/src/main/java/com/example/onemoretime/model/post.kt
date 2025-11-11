package com.example.onemoretime.model

import androidx.annotation.DrawableRes
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
    // @DrawableRes val image: Int? = null, // Room no puede guardar referencias a Drawables directamente
    val upvotes: Int,
    val comments: Int,
    val rating: Float
)
