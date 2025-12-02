package com.example.onemoretime.data

import android.content.Context
import com.example.onemoretime.data.remote.RawgRetrofitInstance
import com.example.onemoretime.data.remote.RetrofitInstance

/**
 * Interfaz del contenedor de dependencias que define todos los repositorios de la app.
 */
interface AppContainer {
    val postRepository: PostRepository
    val usuarioRepository: UsuarioRepository
    val voteRepository: VoteRepository
    val commentRepository: CommentRepository
    val commentVoteRepository: CommentVoteRepository
    val gameRepository: GameRepository // <-- NUEVO
}

/**
 * [AppContainer] implementation that provides instances of the repositories.
 */
class AppDataContainer(private val context: Context) : AppContainer {
    
    override val postRepository: PostRepository by lazy {
        DefaultPostRepository(
            AppDatabase.getDatabase(context).postDao(),
            RetrofitInstance.api 
        )
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

    override val commentVoteRepository: CommentVoteRepository by lazy {
        OfflineCommentVoteRepository(AppDatabase.getDatabase(context).commentVoteDao())
    }

    /**
     * Implementación para el nuevo [gameRepository].
     */
    override val gameRepository: GameRepository by lazy {
        NetworkGameRepository(RawgRetrofitInstance.api)
    }
}
