package com.example.onemoretime.data

import android.content.Context

interface AppContainer {
    val postRepository: PostRepository
    val usuarioRepository: UsuarioRepository
    val voteRepository: VoteRepository
    val commentRepository: CommentRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val postRepository: PostRepository by lazy {
        OfflinePostRepository(AppDatabase.getDatabase(context).postDao())
    }

    override val usuarioRepository: UsuarioRepository by lazy {
        OfflineUsuarioRepository(AppDatabase.getDatabase(context).usuarioDao())
    }

    override val voteRepository: VoteRepository by lazy {
        OfflineVoteRepository(AppDatabase.getDatabase(context).voteDao())
    }

    override val commentRepository: CommentRepository by lazy {
        OfflineCommentRepository(AppDatabase.getDatabase(context).commentDao())
    }
}
