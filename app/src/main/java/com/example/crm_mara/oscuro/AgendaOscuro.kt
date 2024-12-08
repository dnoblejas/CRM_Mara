package com.example.crm_mara.oscuro

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
fun AgendaOscuro(navHostController: NavHostController) {
    // Estados
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val savedCitas = remember { mutableStateListOf<Cita>() }

    // Campos para agregar citas
    var cliente by remember { mutableStateOf(TextFieldValue("")) }
    var telefono by remember { mutableStateOf(TextFieldValue("")) }
    var hora by remember { mutableStateOf(TextFieldValue("")) }

    // Colores para el modo oscuro
    val backgroundColor = Color(0xFF0E0B2E)
    val textColor = Color.White

    // Estados para el calendario
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1).dayOfWeek.value % 7 // Para alinear días

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Icono de flecha hacia atrás
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver atrás",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navHostController.popBackStack() },
                tint = Color.White
            )
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
                color = textColor,
                modifier = Modifier.padding(top = 48.dp, bottom = 16.dp)
            )

            // Controles para cambiar de mes
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
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Text("<", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = "${currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentYearMonth.year}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Button(
                    onClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Text(">", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Calendario
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
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                repeat(firstDayOfMonth) {
                    item { Spacer(modifier = Modifier.height(40.dp)) }
                }
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
                                .clickable { selectedDate = date }
                                .padding(8.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                color = if (selectedDate == date) Color.Blue else textColor
                            )
                        }
                    }
                }
            }

            selectedDate?.let {
                Text(
                    text = "Fecha seleccionada: $it",
                    fontSize = 16.sp,
                    color = textColor,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Formulario para agregar citas
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text("Nombre del Cliente", color = Color.Gray) },
                textStyle = androidx.compose.ui.text.TextStyle(color = textColor),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono", color = Color.Gray) },
                textStyle = androidx.compose.ui.text.TextStyle(color = textColor),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (ej. 18:00)", color = Color.Gray) },
                textStyle = androidx.compose.ui.text.TextStyle(color = textColor),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar cita
            Button(
                onClick = {
                    if (selectedDate != null && cliente.text.isNotBlank() && telefono.text.isNotBlank() && hora.text.isNotBlank()) {
                        savedCitas.add(
                            Cita(
                                cliente = cliente.text,
                                telefono = telefono.text,
                                fecha = selectedDate!!,
                                hora = hora.text
                            )
                        )
                        cliente = TextFieldValue("")
                        telefono = TextFieldValue("")
                        hora = TextFieldValue("")
                        Toast.makeText(
                            navHostController.context,
                            "Cita añadida correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            navHostController.context,
                            "Por favor, rellena todos los campos y selecciona una fecha",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Guardar Cita")
            }

            // Lista de citas guardadas
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Citas Guardadas:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.align(Alignment.Start)
            )
            savedCitas.forEach { cita ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(BorderStroke(1.dp, Color.Gray))
                        .background(Color(0xFF1E1B48))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Cliente: ${cita.cliente}, Fecha: ${cita.fecha}, Hora: ${cita.hora}",
                        color = textColor
                    )
                }
            }
        }
    }
}
