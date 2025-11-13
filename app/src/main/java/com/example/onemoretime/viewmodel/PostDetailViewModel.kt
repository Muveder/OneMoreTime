package com.example.onemoretime.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.CommentRepository
import com.example.onemoretime.data.PostRepository
import com.example.onemoretime.data.SessionManager
import com.example.onemoretime.data.VoteRepository
import com.example.onemoretime.model.Comment
import com.example.onemoretime.model.Post
import com.example.onemoretime.model.UserPostVote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CommentNode(
    val comment: Comment,
    val replies: List<CommentNode>
)

data class PostDetailUiState(
    val post: Post? = null,
    val commentTree: List<CommentNode> = emptyList(),
)

enum class VoteType(val value: Int) {
    UPVOTE(1), DOWNVOTE(-1)
}

@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository,
    private val voteRepository: VoteRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val postId: Int = checkNotNull(savedStateHandle["postId"])

    var newCommentText: String = ""
        private set

    val uiState: StateFlow<PostDetailUiState> = SessionManager.currentUser.flatMapLatest { user ->
        // Solo procedemos si hay un usuario logueado
        if (user == null) return@flatMapLatest flowOf(PostDetailUiState())

        combine(
            postRepository.getPostByIdStream(postId),
            commentRepository.getCommentsForPostStream(postId)
        ) { post, comments ->
            val commentTree = buildCommentTree(comments)
            PostDetailUiState(post = post, commentTree = commentTree)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PostDetailUiState()
    )

    fun onNewCommentChange(text: String) {
        newCommentText = text
    }

    fun postComment(parentId: Int? = null) {
        val currentUser = SessionManager.currentUser.value
        if (newCommentText.isNotBlank() && currentUser != null) {
            viewModelScope.launch {
                commentRepository.insertComment(
                    Comment(
                        postId = postId,
                        author = currentUser.nombre,
                        content = newCommentText,
                        parentId = parentId
                    )
                )
                newCommentText = ""
            }
        }
    }

    fun vote(voteType: VoteType) {
        val currentUser = SessionManager.currentUser.value
        val currentPost = uiState.value.post
        if (currentUser == null || currentPost == null) return

        viewModelScope.launch {
            val existingVote = voteRepository.findVote(currentUser.id, currentPost.id)
            var scoreChange = 0

            if (existingVote == null) {
                voteRepository.insertVote(UserPostVote(currentUser.id, currentPost.id, voteType.value))
                scoreChange = voteType.value
            } else if (existingVote.voteType == voteType.value) {
                voteRepository.deleteVote(existingVote)
                scoreChange = -voteType.value
            } else {
                voteRepository.deleteVote(existingVote)
                voteRepository.insertVote(UserPostVote(currentUser.id, currentPost.id, voteType.value))
                scoreChange = 2 * voteType.value
            }

            if (scoreChange != 0) {
                val updatedPost = currentPost.copy(score = currentPost.score + scoreChange)
                postRepository.updatePost(updatedPost)
            }
        }
    }

    private fun buildCommentTree(comments: List<Comment>, parentId: Int? = null): List<CommentNode> {
        return comments
            .filter { it.parentId == parentId }
            .map { comment ->
                CommentNode(
                    comment = comment,
                    replies = buildCommentTree(comments, comment.id)
                )
            }
    }
}
