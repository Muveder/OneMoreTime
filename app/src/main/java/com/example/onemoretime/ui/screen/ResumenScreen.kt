package com.example.onemoretime.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.onemoretime.viewmodel.UsuarioViewModel

@Composable
fun ResumenScreen(navController: NavController, usuarioViewModel: UsuarioViewModel) {
    val uiState by usuarioViewModel.uiState.collectAsState()

    // Colores para el gradiente
    val darkPurple = Color(0xFF4A0072)
    val lightPurple = Color(0xFFB57EDC)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(darkPurple, lightPurple))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡Registro Exitoso!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(24.dp))

            // --- Resumen de datos ---
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                Text("Resumen de tu cuenta:", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Nombre: ${uiState.nombre}", color = Color.White, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Correo: ${uiState.correo}", color = Color.White, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Dirección: ${uiState.direccion}", color = Color.White, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Términos aceptados: ${if (uiState.aceptaTerminos) "Sí" else "No"}", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para ir al Home
            Button(
                onClick = {
                    // Navegamos al home, limpiando la pila de navegación para que el usuario no pueda volver al registro
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Continuar a la Home", color = darkPurple, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
