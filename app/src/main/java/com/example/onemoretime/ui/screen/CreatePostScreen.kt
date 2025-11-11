package com.example.onemoretime.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.CreatePostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
    viewModel: CreatePostViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val postUiState = viewModel.postUiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Reseña") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    Button(onClick = {
                        viewModel.savePost()
                        navController.popBackStack() // Volver a la pantalla anterior
                    }) {
                        Text("Publicar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo para el Título
            OutlinedTextField(
                value = postUiState.title,
                onValueChange = { viewModel.updateUiState(postUiState.copy(title = it)) },
                label = { Text("Título de la reseña") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo para la Comunidad/Juego
            OutlinedTextField(
                value = postUiState.community,
                onValueChange = { viewModel.updateUiState(postUiState.copy(community = it)) },
                label = { Text("Comunidad o Juego (ej: eldenring)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Selector de Estrellas para la Valoración
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Tu valoración:", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                RatingInputBar(
                    currentRating = postUiState.rating,
                    onRatingChanged = { newRating ->
                        viewModel.updateUiState(postUiState.copy(rating = newRating))
                    }
                )
            }

            // Campo para el Contenido
            OutlinedTextField(
                value = postUiState.content,
                onValueChange = { viewModel.updateUiState(postUiState.copy(content = it)) },
                label = { Text("Escribe tu reseña aquí...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Para que ocupe el resto del espacio
            )
        }
    }
}

// Suponiendo que tienes un Composable similar a este en algún lugar
@Composable
fun RatingInputBar(currentRating: Float, onRatingChanged: (Float) -> Unit) {
    Row {
        (1..5).forEach { star ->
            Icon(
                imageVector = Icons.Default.Star, 
                contentDescription = null,
                tint = if (star <= currentRating) Color.Yellow else Color.Gray,
                modifier = Modifier.clickable { onRatingChanged(star.toFloat()) }.size(32.dp)
            )
        }
    }
}
