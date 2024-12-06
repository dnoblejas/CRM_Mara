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
import com.example.crm_mara.ui.theme.ZendotsFamily
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroOscuro(navController: NavController) {
    val backgroundColor = Color(0xFF0E0B2E)
    val textColor = Color.White
    val buttonColor = Color.White

    Scaffold(
        topBar = {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Ir atrás", tint = textColor)
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

                OutlinedTextField(
                    value = nif,
                    onValueChange = { nif = it },
                    label = { Text("NIF", fontFamily = ZendotsFamily, fontSize = 15.sp, color = textColor) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre", fontFamily = ZendotsFamily, fontSize = 15.sp, color = textColor) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección", fontFamily = ZendotsFamily, fontSize = 15.sp, color = textColor) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono", fontFamily = ZendotsFamily, fontSize = 15.sp, color = textColor) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    value = contraseña,
                    onValueChange = { contraseña = it },
                    label = { Text("Contraseña", fontFamily = ZendotsFamily, fontSize = 15.sp, color = textColor) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = {
                        // Validaciones y lógica
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = backgroundColor, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Guardar",
                            color = backgroundColor,
                            fontFamily = ZendotsFamily,
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = mensajeConfirmacion,
                    fontFamily = ZendotsFamily,
                    fontSize = 16.sp,
                    color = if (mensajeConfirmacion.contains("correctamente")) Color.Green else Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
