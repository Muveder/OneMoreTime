package com.example.onemoretime.ui.screen

import androidx.compose.foundation.layout.*
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
import com.example.onemoretime.viewmodel.HomeViewModel

val darkPurple = Color(0xFF4A0072)
val lightPurple = Color(0xFFB57EDC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val posts by homeViewModel.posts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OneMoreTime", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkPurple),
                actions = {
                    IconButton(onClick = { /* TODO: Lógica de búsqueda */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = darkPurple) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    IconButton(onClick = {
                        navController.navigate("home") {
                            launchSingleTop = true
                        }
                    }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = lightPurple)
                    }
                    IconButton(onClick = {
                        navController.navigate("explore") {
                            launchSingleTop = true
                        }
                    }) {
                        Icon(Icons.Default.Explore, contentDescription = "Explorar", tint = Color.White)
                    }
                    IconButton(onClick = {
                        navController.navigate("create_post") {
                            launchSingleTop = true
                        }
                    }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Crear Post", tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                    IconButton(onClick = {
                        navController.navigate("profile") {
                            launchSingleTop = true
                        }
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
