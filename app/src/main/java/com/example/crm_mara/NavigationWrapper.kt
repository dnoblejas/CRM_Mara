package com.example.crm_mara

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.crm_mara.oscuro.AgendaOscuro
import com.example.crm_mara.oscuro.InicioSesionOscuro
import com.example.crm_mara.oscuro.RegistroOscuro
import com.example.crm_mara.oscuro.TiposCortesOscuro
import com.example.crm_mara.scenes.Agenda
import com.example.crm_mara.scenes.Cancelar
import com.example.crm_mara.scenes.CitasClientes
import com.example.crm_mara.scenes.InfoClientes
import com.example.crm_mara.scenes.InicioSesion
import com.example.crm_mara.scenes.TiposCortes
import com.example.crm_mara.scenes.Registro
import com.example.crm_mara.scenes.ReseñasScreen


@Composable
fun NavigationWrapper (navHostController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val usuarioActual = sharedPreferences.getString("usuario_actual", "UsuarioInvitado") ?: "UsuarioInvitado"


    NavHost(navController = navHostController, startDestination = "InicioSesionOscuro") {

        // Modo día

        composable("InicioSesion") { InicioSesion(navHostController) }
        composable("Agenda") { Agenda(navHostController) }
        composable("Cancelar") { Cancelar(navHostController) }
        composable("CitasClientes") { CitasClientes(navHostController) }
        composable("InfoClientes") { InfoClientes(navHostController) }
        composable("Registro") { Registro(navHostController) }
        composable("TiposCortes") { TiposCortes(navHostController) }
        composable("ReseñasScreen") { ReseñasScreen(usuarioActual = usuarioActual) }
        // Modo Oscuro
        composable("InicioSesionOscuro") { InicioSesionOscuro(navHostController) }
        composable("TiposCortesOscuro") { TiposCortesOscuro(navHostController) }
        composable("AgendaOscuro") { AgendaOscuro(navHostController) }
        composable("RegistroOscuro") { RegistroOscuro(navHostController) }

    }
}