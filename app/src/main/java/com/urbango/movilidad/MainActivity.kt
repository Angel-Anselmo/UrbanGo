package com.urbango.movilidad

import com.urbango.movilidad.ui.MapScreen
import com.urbango.movilidad.ui.CardScreen
import com.urbango.movilidad.ui.ProcedureScreen
import com.urbango.movilidad.ui.ReportScreen
import com.urbango.movilidad.ui.HomeScreen // Nueva importación
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var selectedItem by remember { mutableIntStateOf(0) }

    // Lista de nombres de pestañas
    val items = listOf("Inicio", "Tarjeta", "Rutas", "Trámites", "Reportes")

    // Lista de iconos correspondientes
    val icons = listOf(
        Icons.Default.Home,   // Inicio
        Icons.Default.CreditCard, // Tarjeta
        Icons.Default.DirectionsBus, // Rutas
        Icons.Default.Description, // Trámites
        Icons.Default.Report // Reportes
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.White,
                contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedItem) {
            0 -> HomeScreen(modifier = Modifier.padding(innerPadding)) // Cambiado de Greeting a HomeScreen
            1 -> CardScreen(modifier = Modifier.padding(innerPadding))
            2 -> MapScreen(modifier = Modifier.padding(innerPadding))
            3 -> ProcedureScreen(modifier = Modifier.padding(innerPadding))
            4 -> ReportScreen(modifier = Modifier.padding(innerPadding))
        }
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