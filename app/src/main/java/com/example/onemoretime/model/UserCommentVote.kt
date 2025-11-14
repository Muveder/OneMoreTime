package com.example.onemoretime.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_comment_votes",
    primaryKeys = ["userId", "commentId"], // Un usuario solo puede tener un voto por comentario
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Comment::class,
            parentColumns = ["id"],
            childColumns = ["commentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserCommentVote(
    val userId: Int,
    val commentId: Int,
    val voteType: Int // 1 para upvote, -1 para downvote
)
