package com.example.onemoretime.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.onemoretime.model.UserPostVote

@Dao
interface VoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVote(vote: UserPostVote)

    @Query("SELECT * FROM user_post_votes WHERE userId = :userId AND postId = :postId")
    suspend fun findVote(userId: Int, postId: Int): UserPostVote?

    @Delete
    suspend fun deleteVote(vote: UserPostVote)
}
