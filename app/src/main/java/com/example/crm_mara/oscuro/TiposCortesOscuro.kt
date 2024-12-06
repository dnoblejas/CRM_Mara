package com.example.crm_mara.oscuro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.crm_mara.R
import com.example.crm_mara.ui.theme.ZendotsFamily

// Modelo de datos
data class Corte(val name: String, val price: String, val imageRes: Int)

@Composable
fun TiposCortesOscuro(navHostController: NavHostController) {
    // Datos de los cortes
    val cortes = listOf(
        Corte("Corte 1", "$10", R.drawable.mara1),
        Corte("Corte 2", "$15", R.drawable.mara2),
        Corte("Corte 3", "$20", R.drawable.mara3),
        Corte("Corte 4", "$25", R.drawable.mara4),
        Corte("Corte 5", "$30", R.drawable.mara5),
        Corte("Corte 6", "$35", R.drawable.mara6),
        Corte("Corte 7", "$12", R.drawable.mara7),
        Corte("Corte 8", "$18", R.drawable.mara8),
        Corte("Corte 9", "$22", R.drawable.mara9),
        Corte("Corte 10", "$28", R.drawable.mara10),
        Corte("Corte 11", "$32", R.drawable.mara11),
        Corte("Corte 12", "$40", R.drawable.mara12)
    )

    // Colores para el modo oscuro
    val backgroundColor = Color(0xFF0E0B2E)
    val textColor = Color.White

    Box(
        modifier = Modifier
            .fillMaxSize() // Asegura que el Box ocupe toda la pantalla
            .background(backgroundColor) // Fondo personalizado
    ) {
        // Contenedor principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título
            Text(
                text = "Cortes",
                fontSize = 26.sp,
                fontFamily = ZendotsFamily,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp),
                color = Color.White
            )

            // Grid de elementos
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f) // Ocupa el espacio disponible menos el de los botones
            ) {
                items(cortes.size) { index ->
                    CorteItem(corte = cortes[index])
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
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
                    onClick = { navHostController.navigate("citasClientes") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = "Ir a Citas Clientes",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CorteItem(corte: Corte) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = corte.imageRes),
            contentDescription = corte.name,
            modifier = Modifier
                .size(100.dp)
                .border(1.dp, Color.Black)
        )
        Text(
            text = corte.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = corte.price,
            fontSize = 14.sp,
            fontFamily = ZendotsFamily,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
