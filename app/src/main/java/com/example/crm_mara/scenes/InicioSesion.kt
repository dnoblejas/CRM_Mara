package com.example.crm_mara.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crm_mara.ui.theme.ZendotsFamily
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun InicioSesion(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp)
    ) {
        // Texto dividido en dos líneas
        Text(
            text = "MA",
            color = Color.Black,
            fontSize = 90.sp,
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = ZendotsFamily,
                fontSize = 90.sp
            )
        )
        Text(
            text = "RA",
            color = Color.Black,
            fontSize = 90.sp,
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = ZendotsFamily,
                fontSize = 90.sp
            )
        )
        Spacer(modifier = Modifier.size(48.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = contraseña,
            onValueChange = { contraseña = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(24.dp))

        if (loginError.isNotEmpty()) {
            Text(text = loginError, color = Color.Red)
        }

        Button(
            onClick = {
                if (nombre.isEmpty() || contraseña.isEmpty()) {
                    loginError = "Por favor, completa todos los campos"
                    return@Button
                }

                isLoading = true
                db.collection("clientes")
                    .whereEqualTo("nombre", nombre)
                    .whereEqualTo("contraseña", contraseña)
                    .get()
                    .addOnSuccessListener { documents ->
                        isLoading = false
                        if (!documents.isEmpty) {
                            navController.navigate("TiposCortes")
                        } else {
                            loginError = "Usuario o contraseña incorrectos"
                        }
                    }
                    .addOnFailureListener {
                        isLoading = false
                        loginError = "Error de conexión, intenta de nuevo"
                    }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Iniciar Sesión", color = Color.White, fontSize = 20.sp)
            }
        }

        Button(
            onClick = { navController.navigate("Registro") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(text = "Registro", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.size(32.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /* Cambiar al tema claro */ }, modifier = Modifier.size(48.dp)) {
                Icon(imageVector = Icons.Filled.WbSunny, contentDescription = "Modo claro", tint = Color.Black)
            }
            Spacer(modifier = Modifier.size(16.dp))
            IconButton(onClick = { /* Cambiar al tema oscuro */ }, modifier = Modifier.size(48.dp)) {
                Icon(imageVector = Icons.Filled.DarkMode, contentDescription = "Modo oscuro", tint = Color.Black)
            }
        }
    }
}
