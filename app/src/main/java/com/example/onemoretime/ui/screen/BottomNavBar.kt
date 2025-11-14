package com.example.onemoretime.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String?
) {
    BottomAppBar(containerColor = darkPurple) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            NavBarIcon(navController = navController, route = "home", icon = Icons.Default.Home, currentRoute = currentRoute)
            NavBarIcon(navController = navController, route = "explore", icon = Icons.Default.Explore, currentRoute = currentRoute)
            IconButton(onClick = { navController.navigate("create_post") { launchSingleTop = true } }) {
                Icon(Icons.Default.AddCircle, contentDescription = "Crear Post", tint = Color.White, modifier = Modifier.size(40.dp))
            }
            NavBarIcon(navController = navController, route = "profile", icon = Icons.Default.Person, currentRoute = currentRoute)
        }
    }
}

@Composable
private fun NavBarIcon(
    navController: NavController,
    route: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    currentRoute: String?
) {
    val isSelected = currentRoute == route
    IconButton(onClick = { 
        if (!isSelected) {
            navController.navigate(route) { launchSingleTop = true }
        }
    }) {
        Icon(
            imageVector = icon, 
            contentDescription = route.capitalize(), 
            tint = if (isSelected) lightPurple else Color.White
        )
    }
}
