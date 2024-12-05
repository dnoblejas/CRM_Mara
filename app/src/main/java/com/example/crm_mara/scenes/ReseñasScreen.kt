package com.example.crm_mara.scenes

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Modelo de datos para una reseña con un campo 'id' único
data class Reseña(
    val id: String = "",
    val nombreUsuario: String = "",
    val comentario: String = "",
    val calificacion: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReseñasScreen(usuarioActual: String, navHostController: NavHostController) {
    val context = LocalContext.current

    // Instancia de Firebase Firestore
    val db = Firebase.firestore

    // Estado de las reseñas cargadas
    var reseñas by remember { mutableStateOf<List<Reseña>>(emptyList()) }

    // Variables de estado para el formulario
    var comentario by remember { mutableStateOf(TextFieldValue("")) }
    var calificacion by remember { mutableStateOf(0) }
    var errorMensaje by remember { mutableStateOf("") }

    // Cargar las reseñas desde Firestore
    LaunchedEffect(Unit) {
        db.collection("reseñas")
            .get()
            .addOnSuccessListener { documents ->
                val listaReseñas = documents.mapNotNull { it.toObject(Reseña::class.java) }
                reseñas = listaReseñas
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al cargar las reseñas: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título de la sección de reseñas
        Text(
            text = "Reseñas",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 24.dp),
            color = Color.Black
        )

        // Campo para comentario
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = comentario,
            onValueChange = { comentario = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Escribe tu comentario") },
            singleLine = false,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            )
        )

        // Selector de estrellas para la calificación
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Calificación: ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            for (i in 1..5) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Estrella",
                    tint = if (i <= calificacion) Color.Yellow else Color.Gray,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .clickable { calificacion = i }
                )
            }
        }

        // Botón para añadir la reseña
        Button(
            onClick = {
                if (comentario.text.isNotBlank()) {
                    val nuevaReseña = Reseña(
                        id = db.collection("reseñas").document().id,  // Crear un ID único
                        nombreUsuario = usuarioActual,
                        comentario = comentario.text,
                        calificacion = calificacion
                    )

                    // Guardar reseña en Firebase
                    db.collection("reseñas")
                        .document(nuevaReseña.id)
                        .set(nuevaReseña)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Reseña añadida", Toast.LENGTH_SHORT).show()

                            // Actualizar estado local
                            reseñas = reseñas + nuevaReseña

                            // Limpiar campos después de agregar reseña
                            comentario = TextFieldValue("")
                            calificacion = 0
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error al añadir la reseña: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    errorMensaje = "El comentario no puede estar vacío."
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        ) {
            Text("Añadir Reseña")
        }

        if (errorMensaje.isNotEmpty()) {
            Text(text = errorMensaje, color = Color.Red, modifier = Modifier.padding(8.dp))
        }

        // Mostrar las reseñas existentes
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Reseñas existentes:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        Column {
            reseñas.forEachIndexed { index, reseña ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.Black)
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Usuario: ${reseña.nombreUsuario}", fontWeight = FontWeight.Bold)
                            Text(text = "Comentario: ${reseña.comentario}")
                            Row {
                                Text("Calificación: ", fontWeight = FontWeight.Bold)
                                repeat(reseña.calificacion) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Estrella",
                                        tint = Color.Yellow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Borrar reseña",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    // Eliminar la reseña de Firebase
                                    db.collection("reseñas")
                                        .document(reseña.id)
                                        .delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Reseña eliminada", Toast.LENGTH_SHORT).show()
                                            reseñas = reseñas.toMutableList().apply { removeAt(index) }
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Error al eliminar la reseña: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                        )
                    }
                }
            }
        }

        // Spacer para empujar los botones hacia la parte inferior
        Spacer(modifier = Modifier.weight(1f))

        // Botones de navegación en la parte inferior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navHostController.navigate("agenda") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Ir a Agenda",
                    modifier = Modifier.size(24.dp)
                )
            }
            Button(
                onClick = { navHostController.navigate("tiposCortes") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Cortes",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
