package com.example.onemoretime.data

import com.example.onemoretime.model.UserPostVote

interface VoteRepository {
    suspend fun findVote(userId: Int, postId: Int): UserPostVote?
    suspend fun insertVote(vote: UserPostVote)
    suspend fun deleteVote(vote: UserPostVote)
}

class OfflineVoteRepository(private val voteDao: VoteDao) : VoteRepository {
    override suspend fun findVote(userId: Int, postId: Int): UserPostVote? = voteDao.findVote(userId, postId)
    override suspend fun insertVote(vote: UserPostVote) = voteDao.insertVote(vote)
    override suspend fun deleteVote(vote: UserPostVote) = voteDao.deleteVote(vote)
}
