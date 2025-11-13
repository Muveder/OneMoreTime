package com.example.onemoretime.data

import com.example.onemoretime.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getCommentsForPostStream(postId: Int): Flow<List<Comment>>
    suspend fun insertComment(comment: Comment)
}

class OfflineCommentRepository(private val commentDao: CommentDao) : CommentRepository {
    override fun getCommentsForPostStream(postId: Int): Flow<List<Comment>> = commentDao.getCommentsForPost(postId)
    override suspend fun insertComment(comment: Comment) = commentDao.insertComment(comment)
}
