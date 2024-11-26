package com.example.crm_mara.scenes

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val hora: String
)

@Composable
fun Agenda(navController: NavController) {
    // Estados
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val savedCitas = remember { mutableStateListOf<Cita>() }

    // Campos para agregar citas
    var cliente by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    // Estados para el calendario
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1).dayOfWeek.value % 7 // Para alinear días

    // Instancia de Firestore
    val db = FirebaseFirestore.getInstance()

    // Función para guardar una cita en Firestore
    suspend fun guardarCitaEnFirestore(cliente: String, telefono: String, fecha: LocalDate, hora: String) {
        val cita = hashMapOf(
            "cliente" to cliente,
            "telefono" to telefono,
            "fecha" to fecha.toString(), // Guarda la fecha como String o como Timestamp
            "hora" to hora
        )

        // Guardar la cita en la colección 'citas'
        db.collection("citas")
            .add(cita) // O puedes usar .document(citaId).set(cita) para asignar un id personalizado
            .addOnSuccessListener {
                Toast.makeText(navController.context, "Cita añadida correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(navController.context, "Error al añadir cita: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Función para cargar las citas desde Firestore
    suspend fun cargarCitasDesdeFirestore() {
        val result = db.collection("citas").get().await()
        savedCitas.clear()

        for (document in result) {
            val cliente = document.getString("cliente") ?: ""
            val telefono = document.getString("telefono") ?: ""
            val fecha = document.getString("fecha") ?: ""
            val hora = document.getString("hora") ?: ""

            // Convertir la fecha de String a LocalDate si es necesario
            val localDate = LocalDate.parse(fecha)

            savedCitas.add(Cita(cliente, telefono, localDate, hora))
        }
    }

    // Cargar las citas cuando la composición se ejecute
    LaunchedEffect(Unit) {
        cargarCitasDesdeFirestore()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Agenda",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Controles para cambiar de mes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // Botón de "Mes Anterior"
            Button(
                onClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(36.dp) // Tamaño más pequeño
            ) {
                Text("<", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            // Título del mes y año
            Text(
                text = "${currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentYearMonth.year}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Botón de "Mes Siguiente"
            Button(
                onClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(36.dp) // Tamaño más pequeño
            ) {
                Text(">", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Calendario
        LazyVerticalGrid(
            columns = GridCells.Fixed(7), // Días de la semana
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            // Encabezado: Días de la semana
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

            // Espacios vacíos antes del primer día del mes
            repeat(firstDayOfMonth) {
                item { Spacer(modifier = Modifier.height(40.dp)) }
            }

            // Días del mes
            for (day in 1..daysInMonth) {
                val date = currentYearMonth.atDay(day)
                item {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .border(
                                BorderStroke(
                                    if (selectedDate == date) 2.dp else 1.dp,
                                    if (selectedDate == date) Color.Blue else Color.Gray
                                )
                            )
                            .clickable { selectedDate = date } // Seleccionar el día
                            .padding(8.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.toString(),
                            color = if (selectedDate == date) Color.Blue else Color.Black
                        )
                    }
                }
            }
        }

        // Mostrar fecha seleccionada
        selectedDate?.let {
            Text(
                text = "Fecha seleccionada: $it",
                modifier = Modifier.padding(top = 16.dp),
                fontSize = 16.sp
            )
        }

        // Formulario para agregar citas
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

        // Botón para guardar cita
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (selectedDate != null && cliente.isNotBlank() && telefono.isNotBlank() && hora.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        guardarCitaEnFirestore(cliente, telefono, selectedDate!!, hora)
                    }
                    cliente = ""
                    telefono = ""
                    hora = ""
                } else {
                    Toast.makeText(
                        navController.context,
                        "Por favor, rellena todos los campos y selecciona una fecha",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Guardar Cita")
        }

        // Mostrar las citas guardadas
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier.fillMaxWidth()
        ) {
            savedCitas.forEach { cita ->
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(BorderStroke(1.dp, Color.Black))
                            .padding(8.dp)
                    ) {
                        Column {
                            Text("Cliente: ${cita.cliente}")
                            Text("Teléfono: ${cita.telefono}")
                            Text("Fecha: ${cita.fecha}")
                            Text("Hora: ${cita.hora}")
                        }
                    }
                }
            }
        }
    }
}
