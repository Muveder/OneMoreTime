package com.example.onemoretime.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val posts = uiState.postList

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkPurple)
            )
        },
        bottomBar = {
            // Reutilizamos la misma barra de navegación
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
                        Icon(Icons.Default.Explore, contentDescription = "Explorar", tint = Color.White)
                    }
                    IconButton(onClick = {
                        navController.navigate("create_post") { launchSingleTop = true }
                    }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Crear Post", tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                    IconButton(onClick = {
                        navController.navigate("profile") { launchSingleTop = true }
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", tint = lightPurple) // Color activo
                    }
                }
            }
        },
        containerColor = lightPurple.copy(alpha = 0.1f)
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Cabecera del Perfil
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkPurple.copy(alpha = 0.5f))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Placeholder para el nombre de usuario
                Text("Erica", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text("u/Erica", fontSize = 16.sp, color = Color.Gray)
            }

            // Lista de posts del usuario
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(posts) { post ->
                    PostCard(post = post, navController = navController)
                }
            }
        }
    }
}
