package com.example.crm_mara.scenes

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// Modelo de Cita
data class Cita(
    val cliente: String,
    val telefono: String,
    val fecha: LocalDate,
    val hora: String,
    val descripcion: String
)

@Composable
fun Agenda(navController: NavController) {
    // Estados
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val savedCitas = remember { mutableStateListOf<Cita>() }

    // Variables mutables para los campos de entrada
    var cliente by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1).dayOfWeek.value % 7

    val db = FirebaseFirestore.getInstance()

    // Función para guardar cita
    fun guardarCitaEnFirestore(cliente: String, telefono: String, fecha: LocalDate, hora: String, descripcion: String) {
        val cita = hashMapOf(
            "cliente" to cliente,
            "telefono" to telefono,
            "fecha" to fecha.toString(),
            "hora" to hora,
            "descripcion" to descripcion
        )

        println("Guardando cita: $cita") // Verificar qué datos se envían

        db.collection("citas")
            .add(cita)
            .addOnSuccessListener {
                Toast.makeText(navController.context, "Cita añadida correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(navController.context, "Error al añadir cita: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Función para cargar citas
    suspend fun cargarCitasDesdeFirestore() {
        val result = db.collection("citas").get().await()
        savedCitas.clear()

        for (document in result) {
            val cliente = document.getString("cliente") ?: ""
            val telefono = document.getString("telefono") ?: ""
            val fecha = document.getString("fecha") ?: ""
            val hora = document.getString("hora") ?: ""
            val descripcion = document.getString("descripcion") ?: ""

            val localDate = LocalDate.parse(fecha)
            savedCitas.add(Cita(cliente, telefono, localDate, hora, descripcion))
        }
    }

    LaunchedEffect(Unit) {
        cargarCitasDesdeFirestore()
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Agenda",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { currentYearMonth = currentYearMonth.minusMonths(1) }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Mes anterior")
                }
                Text(
                    text = "${currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentYearMonth.year}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                IconButton(onClick = { currentYearMonth = currentYearMonth.plusMonths(1) }) {
                    Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Mes siguiente")
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                val weekDays = listOf("L", "M", "X", "J", "V", "S", "D")
                weekDays.forEach { day ->
                    item {
                        Text(
                            text = day,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp),
                            color = Color.Gray
                        )
                    }
                }
                repeat(firstDayOfMonth) { item { Spacer(modifier = Modifier.height(40.dp)) } }
                for (day in 1..daysInMonth) {
                    val date = runCatching { currentYearMonth.atDay(day) }.getOrNull()
                    item {
                        if (date != null) {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .border(
                                        BorderStroke(
                                            if (selectedDate == date) 2.dp else 1.dp,
                                            if (selectedDate == date) Color.Blue else Color.Gray
                                        )
                                    )
                                    .clickable { selectedDate = date }
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (selectedDate == date) Color.Blue else Color.Black
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }

            selectedDate?.let {
                Text(
                    text = "Fecha seleccionada: $it",
                    modifier = Modifier.padding(top = 16.dp),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text("Nombre del Cliente") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (ej. 18:00)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (selectedDate == null || cliente.isBlank() || telefono.isBlank() || hora.isBlank() || descripcion.isBlank()) {
                        Toast.makeText(navController.context, "Rellena todos los campos y selecciona una fecha", Toast.LENGTH_SHORT).show()
                    } else if (!telefono.matches(Regex("^\\d{9}$"))) {
                        Toast.makeText(navController.context, "El teléfono debe tener 9 dígitos", Toast.LENGTH_SHORT).show()
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarCitaEnFirestore(cliente, telefono, selectedDate!!, hora, descripcion)
                        }
                        cliente = ""
                        telefono = ""
                        hora = ""
                        descripcion = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Guardar Cita")
            }
        }
    }
}
