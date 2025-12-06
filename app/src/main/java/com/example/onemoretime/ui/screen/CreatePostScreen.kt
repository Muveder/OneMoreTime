package com.example.onemoretime.ui.screen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.onemoretime.data.SessionManager
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.CreatePostViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
    createPostViewModel: CreatePostViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by createPostViewModel.uiState.collectAsState()
    // Obtenemos el usuario actual del SessionManager
    val currentUser by SessionManager.currentUser.collectAsState(initial = null)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(it, flag)
                createPostViewModel.onImageSelected(it)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap: Bitmap? ->
            bitmap?.let {
                val uri = saveBitmapToUri(context, it)
                createPostViewModel.onImageSelected(uri)
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                cameraLauncher.launch()
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Reseña", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkPurple),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                actions = {
                    Button(onClick = {
                        coroutineScope.launch {
                            // CORRECCIÓN: Le pasamos el uiState y el currentUser a la función
                            if (createPostViewModel.savePost(uiState, currentUser)) {
                                navController.popBackStack()
                            }
                        }
                    }) {
                        Text("Publicar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(value = uiState.title, onValueChange = { createPostViewModel.updateUiState(uiState.copy(title = it)) }, label = { Text("Título de la reseña") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = uiState.community, onValueChange = { createPostViewModel.updateUiState(uiState.copy(community = it)) }, label = { Text("Comunidad o Juego") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Text("Tu valoración:", style = MaterialTheme.typography.titleMedium)
            
            RatingBar(rating = uiState.rating, onRatingChange = { createPostViewModel.updateUiState(uiState.copy(rating = it)) })

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            ) {
                if (uiState.imageUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = Uri.parse(uiState.imageUrl)),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                TextField(
                    value = uiState.content,
                    onValueChange = { createPostViewModel.updateUiState(uiState.copy(content = it)) },
                    placeholder = { Text("Escribe tu reseña aquí...") },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Galería")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Galería")
                }
                Button(onClick = { cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA) }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cámara")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cámara")
                }
            }
        }
    }
}

private fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
    val fos = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    fos.close()
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    onRatingChange: (Float) -> Unit
) {
    Row(modifier = modifier) {
        (1..5).forEach { index ->
            Icon(
                imageVector = if (index <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onRatingChange(index.toFloat()) }
            )
        }
    }
}
