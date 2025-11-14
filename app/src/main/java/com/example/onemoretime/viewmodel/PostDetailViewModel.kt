package com.example.onemoretime.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onemoretime.data.*
import com.example.onemoretime.model.Comment
import com.example.onemoretime.model.Post
import com.example.onemoretime.model.UserCommentVote
import com.example.onemoretime.model.UserPostVote
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CommentNode(
    val comment: Comment,
    val replies: List<CommentNode>
)

data class PostDetailUiState(
    val post: Post? = null,
    val commentTree: List<CommentNode> = emptyList(),
    val replyingTo: Comment? = null,
    val isLoading: Boolean = true
)

enum class VoteType(val value: Int) {
    UPVOTE(1), DOWNVOTE(-1)
}

class PostDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository,
    private val voteRepository: VoteRepository,
    private val commentRepository: CommentRepository,
    private val commentVoteRepository: CommentVoteRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val postId: Int = checkNotNull(savedStateHandle["postId"])

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    var newCommentText by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            SessionManager.currentUser.collect { user ->
                if (user == null) {
                    _uiState.value = PostDetailUiState(isLoading = false)
                } else {
                    _uiState.update { it.copy(isLoading = true) } // Start loading
                    combine(
                        postRepository.getPostByIdStream(postId),
                        commentRepository.getCommentsForPostStream(postId),
                    ) { post, comments ->
                        val commentTree = buildCommentTree(comments)
                        _uiState.update {
                            it.copy(post = post, commentTree = commentTree, isLoading = false)
                        }
                    }.collect()
                }
            }
        }
    }

    fun onNewCommentChange(text: String) {
        newCommentText = text
    }

    fun onReplyClicked(comment: Comment) {
        _uiState.update { it.copy(replyingTo = comment) }
    }

    fun cancelReply() {
        _uiState.update { it.copy(replyingTo = null) }
    }

    fun postComment() {
        val currentUser = SessionManager.currentUser.value
        val currentPost = _uiState.value.post
        if (newCommentText.isNotBlank() && currentUser != null && currentPost != null) {
            viewModelScope.launch {
                val parentId = _uiState.value.replyingTo?.id
                commentRepository.insertComment(
                    Comment(postId = postId, author = currentUser.nombre, content = newCommentText, parentId = parentId)
                )
                postRepository.updatePost(currentPost.copy(comments = currentPost.comments + 1))
                newCommentText = ""
                cancelReply()
            }
        }
    }

    fun voteOnPost(voteType: VoteType, context: Context) {
        val currentUser = SessionManager.currentUser.value ?: return
        val currentPost = _uiState.value.post ?: return

        viewModelScope.launch {
            val postAuthor = usuarioRepository.getUsuarioPorNombreStream(currentPost.author).first()
            var scoreChange = 0
            val existingVote = voteRepository.findVote(currentUser.id, currentPost.id)

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
                postRepository.updatePost(currentPost.copy(score = currentPost.score + scoreChange))
            }

            if (scoreChange > 0 && postAuthor != null && currentUser.id != postAuthor.id) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    val builder = NotificationCompat.Builder(context, "like_channel")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("¡Nuevo like!")
                        .setContentText("${currentUser.nombre} ha votado positivo tu reseña: \"${currentPost.title}\"")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    with(NotificationManagerCompat.from(context)) {
                        notify(currentPost.id, builder.build())
                    }
                }
            }
        }
    }

    fun voteOnComment(comment: Comment, voteType: VoteType) {
        val currentUser = SessionManager.currentUser.value ?: return
        viewModelScope.launch {
            val existingVote = commentVoteRepository.findVote(currentUser.id, comment.id)
            var scoreChange = 0
            if (existingVote == null) {
                commentVoteRepository.insertVote(UserCommentVote(currentUser.id, comment.id, voteType.value))
                scoreChange = voteType.value
            } else if (existingVote.voteType == voteType.value) {
                commentVoteRepository.deleteVote(existingVote)
                scoreChange = -voteType.value
            } else {
                commentVoteRepository.deleteVote(existingVote)
                commentVoteRepository.insertVote(UserCommentVote(currentUser.id, comment.id, voteType.value))
                scoreChange = 2 * voteType.value
            }
            if (scoreChange != 0) {
                commentRepository.updateComment(comment.copy(score = comment.score + scoreChange))
            }
        }
    }

    private fun buildCommentTree(comments: List<Comment>, parentId: Int? = null): List<CommentNode> {
        return comments
            .filter { it.parentId == parentId }
            .sortedByDescending { it.score }
            .map { comment ->
                CommentNode(
                    comment = comment,
                    replies = buildCommentTree(comments, comment.id)
                )
            }
    }
}
