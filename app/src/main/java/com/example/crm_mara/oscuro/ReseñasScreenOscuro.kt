package com.example.crm_mara.oscuro

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Modelo de datos para una reseña
data class Reseña(
    val id: String = "",
    val nombreUsuario: String = "",
    val comentario: String = "",
    val calificacion: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReseñasScreenOscuro(usuarioActual: String, navHostController: NavHostController) {
    val backgroundColor = Color(0xFF0E0B2E)
    val textColor = Color.White
    val cardBackgroundColor = Color(0xFF1E1B48)

    val db = Firebase.firestore

    // Estado de reseñas y campos de entrada
    var reseñas by remember { mutableStateOf<List<Reseña>>(emptyList()) }
    var comentario by remember { mutableStateOf(TextFieldValue("")) }
    var calificacion by remember { mutableStateOf(0) }
    var errorMensaje by remember { mutableStateOf("") }

    // Cargar reseñas de Firestore
    LaunchedEffect(Unit) {
        db.collection("reseñas")
            .get()
            .addOnSuccessListener { documents ->
                reseñas = documents.mapNotNull { it.toObject(Reseña::class.java) }
            }
            .addOnFailureListener { e ->
                Toast.makeText(navHostController.context, "Error al cargar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Reseñas",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Entrada de comentario
        TextField(
            value = comentario,
            onValueChange = { comentario = it },
            label = { Text("Escribe tu comentario", color = textColor) },
            textStyle = androidx.compose.ui.text.TextStyle(color = textColor),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Selector de estrellas
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Calificación: ", color = textColor, fontWeight = FontWeight.Bold)
            for (i in 1..5) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Estrella",
                    tint = if (i <= calificacion) Color.Yellow else Color.Gray,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { calificacion = i }
                )
            }
        }

        // Botón de añadir reseña
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (comentario.text.isNotBlank()) {
                    val nuevaReseña = Reseña(
                        id = db.collection("reseñas").document().id,
                        nombreUsuario = usuarioActual,
                        comentario = comentario.text,
                        calificacion = calificacion
                    )
                    db.collection("reseñas").document(nuevaReseña.id).set(nuevaReseña)
                        .addOnSuccessListener {
                            reseñas = reseñas + nuevaReseña
                            comentario = TextFieldValue("")
                            calificacion = 0
                            Toast.makeText(navHostController.context, "Reseña añadida", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(navHostController.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    errorMensaje = "El comentario no puede estar vacío."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ) {
            Text("Añadir Reseña")
        }

        if (errorMensaje.isNotEmpty()) {
            Text(errorMensaje, color = Color.Red, modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Reseñas existentes:", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)

        // Mostrar reseñas
        Column(modifier = Modifier.weight(1f)) {
            reseñas.forEach { reseña ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(cardBackgroundColor)
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("Usuario: ${reseña.nombreUsuario}", color = textColor, fontWeight = FontWeight.Bold)
                            Text("Comentario: ${reseña.comentario}", color = textColor)
                            Row {
                                Text("Calificación: ", color = textColor, fontWeight = FontWeight.Bold)
                                repeat(reseña.calificacion) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(16.dp))
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
                                    db.collection("reseñas").document(reseña.id).delete().addOnSuccessListener {
                                        reseñas = reseñas.filter { it.id != reseña.id }
                                    }
                                }
                        )
                    }
                }
            }
        }

        // Botones de navegación en la parte inferior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navHostController.navigate("AgendaOscuro") },
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
                onClick = { navHostController.navigate("TiposCortesOscuro") },
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
