package com.example.crm_mara.oscuro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun InicioSesionOscuro(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()

    // Colores y estilo para modo oscuro
    val backgroundColor = Color(0xFF0E0B2E) // Fondo oscuro
    val textColor = Color.White // Texto blanco
    val buttonColor = Color(0xFF3D3D3D) // Botón gris oscuro

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            // Título dividido en dos líneas con tipografía Zendots
            Text(
                text = "MA",
                color = textColor,
                fontSize = 90.sp,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = ZendotsFamily,
                    fontSize = 90.sp
                )
            )
            Text(
                text = "RA",
                color = textColor,
                fontSize = 90.sp,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = ZendotsFamily,
                    fontSize = 90.sp
                )
            )
            Spacer(modifier = Modifier.size(48.dp))

            // Campo de texto para usuario
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Usuario", color = textColor, fontFamily = ZendotsFamily) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedIndicatorColor = textColor,
                    unfocusedIndicatorColor = textColor
                )
            )
            Spacer(modifier = Modifier.size(16.dp))

            // Campo de texto para contraseña
            OutlinedTextField(
                value = contraseña,
                onValueChange = { contraseña = it },
                label = { Text("Contraseña", color = textColor, fontFamily = ZendotsFamily) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    cursorColor = textColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = textColor,
                    unfocusedIndicatorColor = textColor
                )
            )
            Spacer(modifier = Modifier.size(24.dp))

            // Mensaje de error
            if (loginError.isNotEmpty()) {
                Text(
                    text = loginError,
                    color = Color.Red,
                    fontFamily = ZendotsFamily
                )
            }

            // Botón para iniciar sesión
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
                                navController.navigate("TiposCortesOscuro")
                            } else {
                                loginError = "Usuario o contraseña incorrectos"
                            }
                        }
                        .addOnFailureListener {
                            isLoading = false
                            loginError = "Error de conexión, intenta de nuevo"
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = textColor, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Iniciar Sesión",
                        color = textColor,
                        fontSize = 20.sp,
                        fontFamily = ZendotsFamily
                    )
                }
            }

            // Botón para registro
            Button(
                onClick = { navController.navigate("RegistroOscuro") },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(
                    text = "Registro",
                    color = textColor,
                    fontSize = 20.sp,
                    fontFamily = ZendotsFamily
                )
            }

            Spacer(modifier = Modifier.size(32.dp))

            // Íconos para modo claro y oscuro
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { navController.navigate("InicioSesion") },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.WbSunny,
                        contentDescription = "Modo claro",
                        tint = textColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                IconButton(
                    onClick = { navController.navigate("InicioSesionOscuro") },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.DarkMode,
                        contentDescription = "Modo oscuro",
                        tint = textColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
