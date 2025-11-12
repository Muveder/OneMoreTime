package com.example.onemoretime.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.onemoretime.R
import com.example.onemoretime.viewmodel.AppViewModelProvider
import com.example.onemoretime.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val loginSuccess by remember { derivedStateOf { loginViewModel.loginSuccess } }

    // Navega a home si el login es exitoso
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.navigate("home") {
                // Limpia el backstack para que el usuario no pueda volver al login con el botón de atrás
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    // Colores para el gradiente y TextFields
    val darkPurple = Color(0xFF4A0072)
    val lightPurple = Color(0xFFB57EDC)
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
        focusedBorderColor = Color.White,
        containerColor = Color.Transparent,
        errorBorderColor = MaterialTheme.colorScheme.error
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de One More Time",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("OneMoreTime", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de Correo
            OutlinedTextField(
                value = loginViewModel.emailInput,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = { Text("Correo electrónico", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = loginViewModel.passwordInput,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Contraseña", color = Color.White.copy(alpha = 0.7f)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors
            )

            // Mostrar error de login si existe
            loginViewModel.loginError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Ingresar
            Button(
                onClick = { loginViewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Ingresar", color = darkPurple, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            // Botón para ir a Registro
            TextButton(onClick = { navController.navigate("register_flow") }) {
                Text("¿No tienes una cuenta? Regístrate", color = Color.White)
            }
        }
    }
}
