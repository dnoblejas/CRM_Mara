package com.example.crm_mara.oscuro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.crm_mara.viewmodel.ThemeViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroOscuro(navController: NavController) {
    // Colores para el modo claro
    val backgroundColor = Color(0xFF0E0B2E)
    val textColor = Color.White
    val buttonColor = Color.Black

    Scaffold(
        topBar = {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Ir atrás")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Spacer(modifier = Modifier.size(20.dp))

                var nif by remember { mutableStateOf("") }
                var nombre by remember { mutableStateOf("") }
                var direccion by remember { mutableStateOf("") }
                var telefono by remember { mutableStateOf("") }
                var contraseña by remember { mutableStateOf("") }
                var mensajeConfirmacion by remember { mutableStateOf("") }
                var isLoading by remember { mutableStateOf(false) }

                val db = FirebaseFirestore.getInstance()

                OutlinedTextField(value = nif, onValueChange = { nif = it }, label = { Text("NIF") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    value = contraseña,
                    onValueChange = { contraseña = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = {
                        if (nif.isEmpty() || nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || contraseña.isEmpty()) {
                            mensajeConfirmacion = "Por favor, completa todos los campos"
                            return@Button
                        }

                        if (!Pattern.matches("\\d{8}[A-Za-z]", nif)) {
                            mensajeConfirmacion = "NIF no válido"
                            return@Button
                        }

                        if (!Pattern.matches("\\d{9}", telefono)) {
                            mensajeConfirmacion = "Teléfono no válido"
                            return@Button
                        }

                        isLoading = true
                        db.collection("clientes").document(nif).get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                isLoading = false
                                mensajeConfirmacion = "El NIF ya está registrado"
                            } else {
                                val dato = hashMapOf(
                                    "nif" to nif,
                                    "nombre" to nombre,
                                    "direccion" to direccion,
                                    "telefono" to telefono,
                                    "contraseña" to contraseña
                                )

                                db.collection("clientes")
                                    .document(nif)
                                    .set(dato)
                                    .addOnSuccessListener {
                                        isLoading = false
                                        mensajeConfirmacion = "Datos guardados correctamente"
                                        nif = ""; nombre = ""; direccion = ""; telefono = ""; contraseña = ""
                                    }
                                    .addOnFailureListener {
                                        isLoading = false
                                        mensajeConfirmacion = "No se ha podido guardar"
                                    }
                            }
                        }
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(text = "Guardar", color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = mensajeConfirmacion,
                    color = if (mensajeConfirmacion.contains("correctamente")) Color.Green else Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
