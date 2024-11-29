package com.example.crm_mara

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.crm_mara.scenes.Agenda
import com.example.crm_mara.scenes.Cancelar
import com.example.crm_mara.scenes.CitasClientes
import com.example.crm_mara.scenes.InfoClientes
import com.example.crm_mara.scenes.InicioSesion
import com.example.crm_mara.scenes.TiposCortes
import com.example.crm_mara.scenes.Registro


@Composable
fun NavigationWrapper (navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = "InicioSesion") {

        composable("InicioSesion") { InicioSesion(navHostController) }
        composable("Agenda") { Agenda(navHostController) }
        composable("Cancelar") { Cancelar(navHostController) }
        composable("CitasClientes") { CitasClientes(navHostController) }
        composable("InfoClientes") { InfoClientes(navHostController) }
        composable("Registro") { Registro(navHostController) }
        composable("TiposCortes") { TiposCortes(navHostController) }

    }
}