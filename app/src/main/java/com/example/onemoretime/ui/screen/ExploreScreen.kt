package com.example.onemoretime.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.ExploreTab
import com.example.onemoretime.viewmodel.ExploreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    navController: NavController,
    exploreViewModel: ExploreViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by exploreViewModel.uiState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explorar", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkPurple),
                actions = {
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            val tabs = listOf("Top", "Nuevos")
            TabRow(selectedTabIndex = uiState.selectedTab.ordinal, containerColor = darkPurple) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, color = Color.White) },
                        selected = uiState.selectedTab.ordinal == index,
                        onClick = { exploreViewModel.selectTab(if (index == 0) ExploreTab.TOP else ExploreTab.NEW) }
                    )
                }
            }

            // Mostramos la lista correspondiente a la pestaña seleccionada
            val postsToShow = when (uiState.selectedTab) {
                ExploreTab.TOP -> uiState.topRatedPosts
                ExploreTab.NEW -> uiState.newPosts
            }

            LazyColumn {
                items(postsToShow) { post ->
                    PostCard(post = post, navController = navController)
                }
            }
        }
    }
}
