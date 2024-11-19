package com.example.crm_mara.scenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
            .padding( start = 15.dp, end = 15.dp)
    ) {
        Text(
            text = "MARA",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = contraseña,
            onValueChange = { contraseña = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(24.dp))

        if (loginError.isNotEmpty()) {
            Text(text = loginError, color = Color.Red)
        }

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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEB3B)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(
                text = "Entrar",
                color = Color.Black,
                fontSize = 32.sp
            )
        }

        Button(
            onClick = { navController.navigate("Registro") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEB3B)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(
                text = "Registrarse",
                color = Color.Black,
                fontSize = 20.sp,
                style = TextStyle.Default
            )
        }
    }
}