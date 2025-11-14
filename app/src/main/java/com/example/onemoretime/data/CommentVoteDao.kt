package com.example.onemoretime.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.onemoretime.model.UserCommentVote

@Dao
interface CommentVoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVote(vote: UserCommentVote)

    @Query("SELECT * FROM user_comment_votes WHERE userId = :userId AND commentId = :commentId")
    suspend fun findVote(userId: Int, commentId: Int): UserCommentVote?

    @Delete
    suspend fun deleteVote(vote: UserCommentVote)
}
