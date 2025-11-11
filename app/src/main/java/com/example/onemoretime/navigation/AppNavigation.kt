package com.example.onemoretime.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.onemoretime.ui.screen.CreatePostScreen
import com.example.onemoretime.ui.screen.ExploreScreen
import com.example.onemoretime.ui.screen.HomeScreen
import com.example.onemoretime.ui.screen.LoginScreen
import com.example.onemoretime.ui.screen.ProfileScreen
import com.example.onemoretime.ui.screen.RegistroScreen
import com.example.onemoretime.ui.screen.ResumenScreen
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }

        // Flujo de Registro Anidado
        navigation(startDestination = "register_screen", route = "register_flow") {
            composable("register_screen") { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("register_flow") }
                val sharedViewModel: UsuarioViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    factory = AppViewModelProvider.Factory
                )
                RegistroScreen(navController = navController, usuarioViewModel = sharedViewModel)
            }
            composable("summary_screen") { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("register_flow") }
                val sharedViewModel: UsuarioViewModel = viewModel(
                    viewModelStoreOwner = parentEntry,
                    factory = AppViewModelProvider.Factory
                )
                ResumenScreen(navController = navController, usuarioViewModel = sharedViewModel)
            }
        }

        composable("home") { 
            HomeScreen(navController = navController)
        }

        composable("explore") { 
            ExploreScreen(navController = navController)
        }
        
        composable("create_post") { 
            CreatePostScreen(navController = navController)
        }

        composable("profile") { 
            ProfileScreen(navController = navController)
        }
    }
}
