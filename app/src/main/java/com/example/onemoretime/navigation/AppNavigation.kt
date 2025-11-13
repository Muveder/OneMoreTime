package com.example.onemoretime.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.onemoretime.ui.screen.*
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }

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

        // Nueva ruta para el detalle del post
        composable(
            route = "post_detail/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) {
            PostDetailScreen(navController = navController)
        }
    }
}
