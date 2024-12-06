package com.example.crm_mara.oscuro

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.crm_mara.ui.theme.ZendotsFamily
import java.time.LocalDate
import java.time.YearMonth

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
    var cliente by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    // Colores para el modo oscuro
    val backgroundColor = Color(0xFF0E0B2E)
    val textColor = Color.White
    val buttonColor = Color.Black

    // Estados para el calendario
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1).dayOfWeek.value % 7 // Para alinear días

    Box(
        modifier = Modifier
            .fillMaxSize() // Asegura que el Box ocupe toda la pantalla
            .background(backgroundColor) // Fondo personalizado
    ) {
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
                color = Color.White,
                fontFamily = ZendotsFamily,
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
                    Text("<", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = ZendotsFamily)
                }

                // Título del mes y año
                Text(
                    text = "${currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentYearMonth.year}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = ZendotsFamily,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Botón de "Mes Siguiente"
                Button(
                    onClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Text(">", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = ZendotsFamily)
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
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
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
                                color = if (selectedDate == date) Color.Blue else Color.White
                            )
                        }
                    }
                }
            }

            // Mostrar fecha seleccionada
            selectedDate?.let {
                Text(
                    text = "Fecha seleccionada: $it",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = ZendotsFamily,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Formulario para agregar citas
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = {
                    Text(
                        text = "Nombre del Cliente",
                        fontFamily = ZendotsFamily
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = ZendotsFamily,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = {
                    Text(
                        text = "Teléfono",
                        fontFamily = ZendotsFamily
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = ZendotsFamily,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = hora,
                onValueChange = { hora = it },
                label = {
                    Text(
                        text = "Hora (ej. 18:00)",
                        fontFamily = ZendotsFamily
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = ZendotsFamily,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar cita
            Button(
                onClick = {
                    if (selectedDate != null && cliente.isNotBlank() && telefono.isNotBlank() && hora.isNotBlank()) {
                        savedCitas.add(
                            Cita(
                                cliente = cliente,
                                telefono = telefono,
                                fecha = selectedDate!!,
                                hora = hora
                            )
                        )
                        cliente = ""
                        telefono = ""
                        hora = ""
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
                Text(
                    text = "Guardar Cita",
                    fontFamily = ZendotsFamily
                )
            }

            // Lista de citas guardadas
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Citas Guardadas:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = ZendotsFamily,
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
                        color = Color.White,
                        fontFamily = ZendotsFamily
                    )
                }
            }
        }
    }
}
