package com.example.crm_mara.scenes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registro(navController: NavController) {

    // Eliminamos el contador de clics en el botón flotante
    Scaffold(
        topBar = {
            // Colocamos la flecha para ir a la pantalla anterior
            IconButton(
                onClick = { navController.popBackStack() }, // Regresar a la pantalla anterior
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Ir atrás"
                )
            }
        }
    ) { innerPadding ->

        // Usamos innerPadding para asegurarnos que el contenido no se superponga a la barra superior
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 50.dp) // Para darle espacio al icono de retroceso
                    .padding(start = 10.dp)
                    .padding(end = 10.dp)
            ) {
                Spacer(modifier = Modifier.size(20.dp))

                var nif by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = nif,
                    onValueChange = { nif = it },
                    label = { Text("NIF") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp) // Espaciado entre los campos
                )

                var nombre by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp) // Espaciado entre los campos
                )

                var direccion by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp) // Espaciado entre los campos
                )

                var telefono by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp) // Espaciado entre los campos
                )

                var contraseña by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = contraseña,
                    onValueChange = { contraseña = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),  // Asegura que el texto se oculte
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp) // Espaciado entre los campos
                )

                Spacer(modifier = Modifier.size(16.dp))

                val db = FirebaseFirestore.getInstance()
                val coleccion = "clientes"

                var mensajeConfirmacion by remember { mutableStateOf("") }

                // Aquí incluimos la contraseña en el HashMap
                val dato = hashMapOf(
                    "nif" to nif,
                    "nombre" to nombre,
                    "direccion" to direccion,
                    "telefono" to telefono,
                    "contraseña" to contraseña
                )

                Button(
                    onClick = {
                        db.collection(coleccion)
                            .document(nif)
                            .set(dato)
                            .addOnSuccessListener {
                                mensajeConfirmacion = "Datos guardados correctamente"
                                // Limpiar los campos después de guardar
                                nif = ""
                                nombre = ""
                                direccion = ""
                                telefono = ""
                                contraseña = ""
                            }
                            .addOnFailureListener {
                                mensajeConfirmacion = "No se ha podido guardar"
                                // Limpiar los campos en caso de error
                                nif = ""
                                nombre = ""
                                direccion = ""
                                telefono = ""
                                contraseña = ""
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(56.dp), // Tamaño mayor del botón
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.medium // Borde redondeado
                ) {
                    Text(text = "Guardar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.size(5.dp))

                Text(text = mensajeConfirmacion, fontWeight = FontWeight.Bold)
            }
        }
    }
}
