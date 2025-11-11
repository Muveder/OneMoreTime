package com.example.onemoretime.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val usuario = uiState.usuario
    val posts = uiState.posts

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(usuario?.nombre ?: "Perfil", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkPurple)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = darkPurple) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    IconButton(onClick = { navController.navigate("home") { launchSingleTop = true } }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                    }
                    IconButton(onClick = { navController.navigate("explore") { launchSingleTop = true } }) {
                        Icon(Icons.Default.Explore, contentDescription = "Explorar", tint = Color.White)
                    }
                    IconButton(onClick = { navController.navigate("create_post") { launchSingleTop = true } }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Crear Post", tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                    IconButton(onClick = { navController.navigate("profile") { launchSingleTop = true } }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", tint = lightPurple) // Color activo
                    }
                }
            }
        },
        containerColor = lightPurple.copy(alpha = 0.1f)
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Cabecera del Perfil
            item {
                if (usuario != null) {
                    ProfileHeader(usuario = usuario, postCount = posts.size)
                }
            }

            // Divisor
            item {
                Divider(color = darkPurple, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            }

            // Título de la sección de posts
            item {
                Text(
                    text = "Mis Reseñas",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            // Lista de posts del usuario
            items(posts) {
                post -> PostCard(post = post, navController = navController)
            }
        }
    }
}

@Composable
fun ProfileHeader(usuario: com.example.onemoretime.model.Usuario, postCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Avatar
        Image(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(lightPurple)
        )

        // Nombre de usuario
        Text(usuario.nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        // Estadísticas
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Statistic(label = "Karma", value = usuario.karma.toString())
            Statistic(label = "Posts", value = postCount.toString())
        }
        
        // Fecha de registro ("Cake Day")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Cake, contentDescription = "Día de tarta", tint = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Día de tarta: ${formatDate(usuario.fechaRegistro)}", color = Color.Gray)
        }
    }
}

@Composable
fun Statistic(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
