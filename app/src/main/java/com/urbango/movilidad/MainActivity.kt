package com.urbango.movilidad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.urbango.movilidad.ui.theme.UrbanGoTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Report


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UrbanGoTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }

    // Lista de nombres de pestañas
    val items = listOf("Inicio", "Tarjeta", "Rutas", "Trámites", "Reportes")

    // Lista de iconos correspondientes
    val icons = listOf(
        Icons.Default.Home,   // Inicio
        Icons.Default.CreditCard, // Tarjeta (necesitas agregar este icono)
        Icons.Default.DirectionsBus, // Rutas (necesitas agregar este icono)
        Icons.Default.Description, // Trámites (necesitas agregar este icono)
        Icons.Default.Report // Reportes (necesitas agregar este icono)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Greeting(name = items[selectedItem], modifier = Modifier.padding(innerPadding))
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UrbanGoTheme {
        MainScreen()
    }
}