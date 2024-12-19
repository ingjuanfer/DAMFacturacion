package com.example.damfacturacion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation() // Oculta la contraseña
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Aquí irá la lógica de autenticación (punto 3)
            if(username.isNotEmpty() && password.isNotEmpty()) {
                onLoginSuccess()
            } else {
                showError = true
            }
        }) {
            Text("Iniciar Sesión")
        }
        if (showError) {
            Text("Por favor, rellene todos los campos.", color = androidx.compose.ui.graphics.Color.Red)
        }
    }
}