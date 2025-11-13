package com.example.onemoretime.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_post_votes",
    primaryKeys = ["userId", "postId"], // Un usuario solo puede tener un voto por post
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Post::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserPostVote(
    val userId: Int,
    val postId: Int,
    val voteType: Int // 1 para upvote, -1 para downvote
)
