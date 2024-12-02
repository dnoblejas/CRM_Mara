package com.example.crm_mara.scenes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crm_mara.viewmodel.ThemeViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun InicioSesion(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }

    // Instancia de Firebase Firestore
    val db = FirebaseFirestore.getInstance()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp)
    ) {
        // Título MARA
        Text(
            text = "MARA DIA",
            color = Color.Black,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(48.dp))

        // Campo de texto para usuario
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(16.dp))

        // Campo de texto para contraseña
        OutlinedTextField(
            value = contraseña,
            onValueChange = { contraseña = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),  // Asegura que el texto se oculte
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(24.dp))

        // Mensaje de error si existe
        if (loginError.isNotEmpty()) {
            Text(text = loginError, color = Color.Red)
        }

        // Botón para iniciar sesión
        Button(
            onClick = {
                if (nombre.isNotEmpty() && contraseña.isNotEmpty()) {
                    db.collection("clientes")
                        .whereEqualTo("nombre", nombre)
                        .whereEqualTo("contraseña", contraseña)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (!documents.isEmpty) {
                                // Usuario encontrado, navega a la pantalla de menú
                                navController.navigate("TiposCortes")
                            } else {
                                // Usuario no encontrado, muestra un mensaje de error
                                loginError = "Usuario no registrado"
                            }
                        }
                        .addOnFailureListener {
                            // Error en la consulta a Firebase
                            loginError = "Error de conexión, intenta de nuevo"
                        }
                } else {
                    loginError = "Por favor, completa todos los campos"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(
                text = "Iniciar Sesión",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        // Botón para registro
        Button(
            onClick = { navController.navigate("Registro") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(
                text = "Registro",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.size(32.dp))

        // Íconos de sol y luna
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Botón de sol (WbSunny)
            IconButton(
                onClick = { navController.navigate("InicioSesion") },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.WbSunny,
                    contentDescription = "Modo claro",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            // Botón de luna (DarkMode)
            IconButton(
                onClick = { navController.navigate("InicioSesionOscuro") },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DarkMode,
                    contentDescription = "Modo oscuro",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
