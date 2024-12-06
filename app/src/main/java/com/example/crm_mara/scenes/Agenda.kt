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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crm_mara.ui.theme.ZendotsFamily
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
    val id: String? = null,
    val cliente: String,
    val telefono: String,
    val fecha: LocalDate,
    val hora: String
)

@Composable
fun Agenda(navController: NavController) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val savedCitas = remember { mutableStateListOf<Cita>() }

    var cliente by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1).dayOfWeek.value % 7

    val db = FirebaseFirestore.getInstance()

    suspend fun guardarOActualizarCitaEnFirestore(cita: Cita) {
        val citaMap = hashMapOf(
            "cliente" to cita.cliente,
            "telefono" to cita.telefono,
            "fecha" to "${cita.fecha}T${cita.hora}",
            "hora" to cita.hora
        )

        if (cita.id == null) {
            db.collection("citas")
                .add(citaMap)
                .addOnSuccessListener {
                    Toast.makeText(navController.context, "Cita añadida correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(navController.context, "Error al añadir cita: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            db.collection("citas")
                .document(cita.id)
                .set(citaMap)
                .addOnSuccessListener {
                    Toast.makeText(navController.context, "Cita actualizada correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(navController.context, "Error al actualizar cita: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    suspend fun cargarCitasDesdeFirestore() {
        val result = db.collection("citas").get().await()
        val newCitas = mutableListOf<Cita>()

        for (document in result) {
            val id = document.id
            val cliente = document.getString("cliente") ?: ""
            val telefono = document.getString("telefono") ?: ""
            val fechaString = document.getString("fecha") ?: ""
            val hora = document.getString("hora") ?: ""

            val parts = fechaString.split("T")
            val localDate = runCatching { LocalDate.parse(parts[0]) }.getOrElse { LocalDate.now() }

            newCitas.add(Cita(id, cliente, telefono, localDate, hora))
        }

        savedCitas.clear()
        savedCitas.addAll(newCitas)
    }

    LaunchedEffect(Unit) {
        cargarCitasDesdeFirestore()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icono de flecha hacia atrás
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver atrás",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.popBackStack() },
                tint = Color.Black
            )
        }

        Text(
            text = "Agenda",
            fontSize = 26.sp,
            fontFamily = ZendotsFamily,
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
            Button(
                onClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(36.dp)
            ) {
                Text("<", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "${currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentYearMonth.year}",
                fontSize = 20.sp,
                fontFamily = ZendotsFamily,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Button(
                onClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(36.dp)
            ) {
                Text(">", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                        fontFamily = ZendotsFamily,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp),
                        color = Color.Gray
                    )
                }
            }

            repeat(firstDayOfMonth) {
                item { Spacer(modifier = Modifier.height(40.dp)) }
            }

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
                                color = if (selectedDate == date) Color.Blue else Color.Black,
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
                fontSize = 16.sp,
                fontFamily = ZendotsFamily
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = cliente,
            onValueChange = { cliente = it },
            label = { Text("Nombre del Cliente") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = ZendotsFamily)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = ZendotsFamily)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (ej. 18:00)") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = ZendotsFamily)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (selectedDate != null && cliente.isNotBlank() && telefono.isNotBlank() && hora.isNotBlank()) {
                    val citaModificada = Cita(
                        id = null,
                        cliente = cliente,
                        telefono = telefono,
                        fecha = selectedDate!!,
                        hora = hora
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        guardarOActualizarCitaEnFirestore(citaModificada)
                        cargarCitasDesdeFirestore()
                    }

                    cliente = ""
                    telefono = ""
                    hora = ""
                    selectedDate = null
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
            Text("Guardar Cita", fontFamily = ZendotsFamily)
        }

        Spacer(modifier = Modifier.height(16.dp))

        savedCitas.forEach { cita ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(BorderStroke(1.dp, Color.Black))
                    .padding(8.dp)
            ) {
                Column {
                    Text("Cliente: ${cita.cliente}", fontFamily = ZendotsFamily)
                    Text("Teléfono: ${cita.telefono}", fontFamily = ZendotsFamily)
                    Text("Fecha: ${cita.fecha}", fontFamily = ZendotsFamily)
                    Text("Hora: ${cita.hora}", fontFamily = ZendotsFamily)
                }
            }
        }
    }
}
