package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.onemoretime.OneMoreTimeApplication

/**
 * Proporciona una Factory para crear instancias de ViewModel para toda la aplicación.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            UsuarioViewModel(oneMoreTimeApplication().container.usuarioRepository)
        }

        initializer {
            PostViewModel(oneMoreTimeApplication().container.postRepository)
        }

        initializer {
            ExploreViewModel(oneMoreTimeApplication().container.postRepository)
        }

        initializer {
            ProfileViewModel(
                oneMoreTimeApplication().container.usuarioRepository,
                oneMoreTimeApplication().container.postRepository
            )
        }

        initializer {
            CreatePostViewModel(oneMoreTimeApplication().container.postRepository)
        }

        initializer {
            LoginViewModel(oneMoreTimeApplication().container.usuarioRepository)
        }

        // Modificado para inyectar los tres repositorios
        initializer {
            PostDetailViewModel(
                this.createSavedStateHandle(),
                oneMoreTimeApplication().container.postRepository,
                oneMoreTimeApplication().container.voteRepository,
                oneMoreTimeApplication().container.commentRepository
            )
        }
    }
}

/**
 * Función de extensión para obtener la instancia de la aplicación desde CreationExtras.
 */
fun CreationExtras.oneMoreTimeApplication(): OneMoreTimeApplication {
    return (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OneMoreTimeApplication)
}
