package com.example.onemoretime.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.ExploreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    navController: NavController,
    exploreViewModel: ExploreViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by exploreViewModel.uiState.collectAsState()
    val posts = uiState.postList

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explorar", color = Color.White) }, // Título cambiado
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkPurple),
                actions = {
                    IconButton(onClick = { /* TODO: Lógica de búsqueda */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            // Reutilizamos la misma barra de navegación que en HomeScreen
            BottomAppBar(containerColor = darkPurple) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    IconButton(onClick = {
                        navController.navigate("home") { launchSingleTop = true }
                    }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                    }
                    IconButton(onClick = {
                        navController.navigate("explore") { launchSingleTop = true }
                    }) {
                        Icon(Icons.Default.Explore, contentDescription = "Explorar", tint = lightPurple) // Color activo
                    }
                    IconButton(onClick = {
                        navController.navigate("create_post") { launchSingleTop = true }
                    }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Crear Post", tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                    IconButton(onClick = {
                        navController.navigate("profile") { launchSingleTop = true }
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
                    }
                }
            }
        },
        containerColor = lightPurple.copy(alpha = 0.1f)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(posts) { post ->
                PostCard(post = post, navController = navController)
            }
        }
    }
}
