package com.example.onemoretime.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class) // Anotación para API experimental
@Composable
fun RegistroScreen(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel
) {
    val uiState by usuarioViewModel.uiState.collectAsState()

    // Navega cuando el registro sea exitoso
    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            navController.navigate("summary_screen")
        }
    }

    // Colores para el gradiente
    val darkPurple = Color(0xFF4A0072)
    val lightPurple = Color(0xFFB57EDC)

    // Colores para los TextFields
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
        focusedBorderColor = Color.White,
        containerColor = Color.Transparent, // Fondo del TextField
        errorBorderColor = MaterialTheme.colorScheme.error,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
        errorLabelColor = MaterialTheme.colorScheme.error,
        errorSupportingTextColor = MaterialTheme.colorScheme.error
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(darkPurple, lightPurple))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()), // Para que el formulario sea scrollable
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear una cuenta", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(modifier = Modifier.height(24.dp))

            // --- Formulario ---
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { usuarioViewModel.onNombreChange(it) },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errores.nombre != null,
                supportingText = { if (uiState.errores.nombre != null) Text(uiState.errores.nombre!!) },
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.correo,
                onValueChange = { usuarioViewModel.onCorreoChange(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errores.correo != null,
                supportingText = { if (uiState.errores.correo != null) Text(uiState.errores.correo!!) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.clave,
                onValueChange = { usuarioViewModel.onClaveChange(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errores.clave != null,
                supportingText = { if (uiState.errores.clave != null) Text(uiState.errores.clave!!) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.direccion,
                onValueChange = { usuarioViewModel.onDireccionChange(it) },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errores.direccion != null,
                supportingText = { if (uiState.errores.direccion != null) Text(uiState.errores.direccion!!) },
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.aceptaTerminos,
                    onCheckedChange = { usuarioViewModel.onAceptaTerminosChange(it) },
                    colors = CheckboxDefaults.colors(checkedColor = Color.White, checkmarkColor = darkPurple)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Acepto los términos y condiciones", color = Color.White)
            }

            if (!uiState.aceptaTerminos && uiState.errores.nombre != null) { // Lógica para mostrar error de términos
                Text(
                    text = "Debes aceptar los términos y condiciones",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { usuarioViewModel.guardarUsuario() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                enabled = uiState.aceptaTerminos
            ) {
                Text("Registrar", color = darkPurple, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            TextButton(onClick = { navController.popBackStack() }) {
                Text("¿Ya tienes una cuenta? Inicia sesión", color = Color.White)
            }
        }
    }
}
