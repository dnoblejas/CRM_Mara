package com.example.crm_mara.oscuro

import androidx.compose.foundation.background
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

import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun InicioSesionOscuro(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }

    // Instancia de Firebase Firestore
    val db = FirebaseFirestore.getInstance()

    // Colores para el modo claro
    val backgroundColor = Color(0xFF0E0B2E)
    val textColor = Color.White
    val buttonColor = Color.Black

    Box(
        modifier = Modifier
            .fillMaxSize() // Asegura que el Box ocupe toda la pantalla
            .background(backgroundColor) // Fondo personalizado
    ) {


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp)
                //.background(backgroundColor)
        ) {
            // Título MARA
            Text(
                text = "MARA NOCHE",
                color = textColor,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(48.dp))

            // Campo de texto para usuario
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Usuario", color = textColor) },
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
                label = { Text("Contraseña", color = textColor) },
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
                                    navController.navigate("TiposCortesOscuro")
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
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }

            // Botón para registro
            Button(
                onClick = { navController.navigate("RegistroOscuro") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(
                    text = "Registro",
                    color = Color.Black,
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
                        tint = textColor,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                // Botón de luna (DarkMode)
                IconButton(
                    onClick = { navController.navigate("InicioSesionOscuro") },  // Aquí cambiamos a la pantalla de "InicioSesion"
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
