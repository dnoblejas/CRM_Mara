import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.crm_mara.oscuro.AgendaOscuro
import com.example.crm_mara.oscuro.InicioSesionOscuro
import com.example.crm_mara.oscuro.RegistroOscuro
import com.example.crm_mara.oscuro.ReseñasScreenOscuro
import com.example.crm_mara.oscuro.TiposCortesOscuro
import com.example.crm_mara.scenes.Agenda
import com.example.crm_mara.scenes.InicioSesion
import com.example.crm_mara.scenes.Registro
import com.example.crm_mara.scenes.ReseñasScreen
import com.example.crm_mara.scenes.TiposCortes

@Composable
fun NavigationWrapper(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = "InicioSesion") {

        // Modo día
        composable("InicioSesion") { InicioSesion(navHostController) }
        composable("Agenda") { Agenda(navHostController) }
        composable("Registro") { Registro(navHostController) }
        composable("TiposCortes") { TiposCortes(navHostController) }
        composable("ReseñasScreen") {
            ReseñasScreen(usuarioActual = "usuarioEjemplo", navHostController = navHostController)
        }

        // Modo Oscuro
        composable("InicioSesionOscuro") { InicioSesionOscuro(navHostController) }
        composable("TiposCortesOscuro") { TiposCortesOscuro(navHostController) }
        composable("AgendaOscuro") { AgendaOscuro(navHostController) }
        composable("RegistroOscuro") { RegistroOscuro(navHostController) }
        composable("ReseñasScreenOscuro") {
            ReseñasScreenOscuro(usuarioActual = "usuarioEjemplo", navHostController = navHostController)
        }
    }
}

