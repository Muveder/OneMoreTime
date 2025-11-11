package com.example.onemoretime.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.onemoretime.OneMoreTimeApplication

/**
 * Proporciona una Factory para crear instancias de ViewModel para toda la aplicación.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer para UsuarioViewModel
        initializer {
            UsuarioViewModel(oneMoreTimeApplication().container.usuarioRepository)
        }

        // Initializer para PostViewModel
        initializer {
            PostViewModel(oneMoreTimeApplication().container.postRepository)
        }

        // Initializer para ExploreViewModel
        initializer {
            ExploreViewModel(oneMoreTimeApplication().container.postRepository)
        }

        // Initializer para ProfileViewModel
        initializer {
            ProfileViewModel(oneMoreTimeApplication().container.postRepository)
        }

        // Puedes añadir más initializers para otros ViewModels aquí
    }
}

/**
 * Función de extensión para obtener la instancia de la aplicación desde CreationExtras.
 */
fun CreationExtras.oneMoreTimeApplication(): OneMoreTimeApplication {
    return (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OneMoreTimeApplication)
}
