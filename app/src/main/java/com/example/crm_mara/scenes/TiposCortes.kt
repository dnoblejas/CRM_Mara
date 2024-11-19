package com.example.crm_mara.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crm_mara.R

// Modelo de datos
data class Corte(val name: String, val price: String, val imageRes: Int)

// Pantalla principal
@Composable
fun TiposCortes(navController: NavController) {
    // Datos de ejemplo con imágenes diferentes
    val cortes = listOf(
        Corte("Corte 1", "$10", R.drawable.mara1),
        Corte("Corte 2", "$15", R.drawable.mara3),
        Corte("Corte 3", "$20", R.drawable.mara3),
        Corte("Corte 4", "$25", R.drawable.mara1),
        Corte("Corte 5", "$30", R.drawable.mara1),
        Corte("Corte 6", "$35", R.drawable.mara3)
    )

    // Layout principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Cortes",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            color = Color.Black
        )

        // Grid de elementos
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columnas
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(cortes.size) { index ->
                CorteItem(corte = cortes[index])
            }
        }
    }
}

// Composable para un elemento de la cuadrícula
@Composable
fun CorteItem(corte: Corte) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(8.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Imagen específica para cada corte
            Image(
                painter = painterResource(id = corte.imageRes),
                contentDescription = corte.name,
                modifier = Modifier
                    .size(64.dp)
                    .padding(top = 8.dp)
            )

            // Título
            Text(
                text = corte.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Precio
            Text(
                text = corte.price,
                fontSize = 14.sp,
                color = Color.Gray
            )

            // Icono de edición
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Usa un recurso drawable
                contentDescription = "Edit Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
