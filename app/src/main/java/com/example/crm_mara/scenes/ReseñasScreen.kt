package com.example.crm_mara.scenes

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Modelo de datos para una reseña
data class Reseña(val nombreUsuario: String, val comentario: String, val calificacion: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReseñasScreen(usuarioActual: String) {
    // Inicialización de Gson y SharedPreferences
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("reseñas_prefs", Context.MODE_PRIVATE)
    val gson = remember { Gson() }

    // Cargar las reseñas desde SharedPreferences
    var reseñas by remember { mutableStateOf(loadReseñas(sharedPreferences, gson)) }

    // Variables de estado para el formulario
    var comentario by remember { mutableStateOf(TextFieldValue("")) }
    var calificacion by remember { mutableStateOf(0) }
    var errorMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                    val nuevaReseña = Reseña(usuarioActual, comentario.text, calificacion)
                    reseñas = reseñas + nuevaReseña
                    saveReseñas(sharedPreferences, gson, reseñas)

                    // Limpiar campos después de agregar reseña
                    comentario = TextFieldValue("")
                    calificacion = 0
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
                                    reseñas = reseñas.toMutableList().also { it.removeAt(index) }
                                    saveReseñas(sharedPreferences, gson, reseñas)
                                }
                        )
                    }
                }
            }
        }
    }
}

// Función para guardar reseñas en SharedPreferences
fun saveReseñas(sharedPreferences: SharedPreferences, gson: Gson, reseñas: List<Reseña>) {
    val editor = sharedPreferences.edit()
    val json = gson.toJson(reseñas)
    editor.putString("reseñas_key", json)
    editor.apply()
}

// Función para cargar reseñas desde SharedPreferences
fun loadReseñas(sharedPreferences: SharedPreferences, gson: Gson): List<Reseña> {
    val json = sharedPreferences.getString("reseñas_key", null)
    val type = object : TypeToken<List<Reseña>>() {}.type
    return if (json != null) gson.fromJson(json, type) else listOf()
}
