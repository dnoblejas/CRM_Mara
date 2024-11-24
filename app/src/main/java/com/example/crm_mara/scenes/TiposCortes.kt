package com.example.crm_mara.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
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

// Modelo de datos
data class Corte(val name: String, val price: String, val imageRes: Int)

@Composable
fun TiposCortes(navHostController: NavHostController) {
    // Datos de los cortes
    val cortes = listOf(
        Corte("Corte 1", "$10", R.drawable.mara1),
        Corte("Corte 2", "$15", R.drawable.mara3),
        Corte("Corte 3", "$20", R.drawable.mara3),
        Corte("Corte 4", "$25", R.drawable.mara1),
        Corte("Corte 5", "$30", R.drawable.mara1),
        Corte("Corte 6", "$35", R.drawable.mara3),
        Corte("Corte 7", "$12", R.drawable.mara1),
        Corte("Corte 8", "$18", R.drawable.mara1),
        Corte("Corte 9", "$22", R.drawable.mara1),
        Corte("Corte 10", "$28", R.drawable.mara1),
        Corte("Corte 11", "$32", R.drawable.mara3),
        Corte("Corte 12", "$40", R.drawable.mara3)
    )

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
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 24.dp),
            color = Color.Black
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

        // Botones de navegación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { navHostController.navigate("agenda") }) {
                Text(text = "Ir a Agenda")
            }
            Button(onClick = { navHostController.navigate("citasClientes") }) {
                Text(text = "Ir a Citas Clientes")
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
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = corte.price,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
