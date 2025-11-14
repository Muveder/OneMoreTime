package com.example.onemoretime.data

import com.example.onemoretime.model.UserCommentVote

interface CommentVoteRepository {
    suspend fun findVote(userId: Int, commentId: Int): UserCommentVote?
    suspend fun insertVote(vote: UserCommentVote)
    suspend fun deleteVote(vote: UserCommentVote)
}

class OfflineCommentVoteRepository(private val commentVoteDao: CommentVoteDao) : CommentVoteRepository {
    override suspend fun findVote(userId: Int, commentId: Int): UserCommentVote? = commentVoteDao.findVote(userId, commentId)
    override suspend fun insertVote(vote: UserCommentVote) = commentVoteDao.insertVote(vote)
    override suspend fun deleteVote(vote: UserCommentVote) = commentVoteDao.deleteVote(vote)
}
