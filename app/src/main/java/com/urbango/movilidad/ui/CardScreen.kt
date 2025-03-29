package com.urbango.movilidad.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen(
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()

    // Estado para el folio de la tarjeta
    var cardFolio by remember { mutableStateOf("") }

    // Estado para controlar si se ha presionado "Confirmar"
    var isConfirmed by remember { mutableStateOf(false) }

    // Estados para controlar las secciones expandibles de "Mis Movimientos"
    var expandedRecargas by remember { mutableStateOf(false) }
    var expandedPagos by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Mi Tarjeta 'YoVoy'",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Estado de Aguascalientes | Coordinación General de Movilidad",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDarkTheme) Color.DarkGray else Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()) // Permite desplazamiento vertical
        ) {
            // Descripción
            Text(
                text = "Para poder brindarte cualquier información sobre tu tarjeta, es necesario ingresar el folio en el siguiente recuadro,",
                fontSize = 14.sp,
                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
            Text(
                text = "Ej: 7006956",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo para ingresar el folio
            OutlinedTextField(
                value = cardFolio,
                onValueChange = { cardFolio = it },
                placeholder = { Text("7006956", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                    unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                    focusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                    unfocusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                    focusedBorderColor = if (isDarkTheme) Color.White else Color.Black,
                    unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color.DarkGray
                )
            )

            // Botón Confirmar
            Button(
                onClick = {
                    // Simulamos que se confirma el folio y se obtienen los datos
                    if (cardFolio.isNotEmpty()) { // Opcional: agregar validación del folio
                        isConfirmed = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51B5), // Color azul similar al de la imagen
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Confirmar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Sección Mi Saldo
            Text(
                text = "Mi Saldo",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.White else Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = if (isConfirmed) {
                    "El saldo actual de tu tarjeta 'Estudiante' es de: $35.25"
                } else {
                    "Ingresa el Folio en el recuadro para saber más sobre esta información."
                },
                fontSize = 14.sp,
                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Sección Mis Movimientos
            Text(
                text = "Mis Movimientos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.White else Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (isConfirmed) {
                // Mostrar opciones expandibles después de confirmar
                Text(
                    text = "Seleccione el movimiento que desee consultar:",
                    fontSize = 14.sp,
                    color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Opción: Ver mis Recargas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ver mis Recargas",
                        fontSize = 16.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                    IconButton(onClick = { expandedRecargas = !expandedRecargas }) {
                        Icon(
                            imageVector = if (expandedRecargas) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = if (expandedRecargas) "Colapsar" else "Expandir",
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                }
                if (expandedRecargas) {
                    // TODO: Mostrar lista de recargas (puede ser un placeholder por ahora)
                    Text(
                        text = "Aquí se mostrarán tus recargas recientes.",
                        fontSize = 14.sp,
                        color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                    )
                }

                // Opción: Ver mis Pagos
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ver mis Pagos",
                        fontSize = 16.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                    IconButton(onClick = { expandedPagos = !expandedPagos }) {
                        Icon(
                            imageVector = if (expandedPagos) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = if (expandedPagos) "Colapsar" else "Expandir",
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                }
                if (expandedPagos) {
                    // TODO: Mostrar lista de pagos (puede ser un placeholder por ahora)
                    Text(
                        text = "Aquí se mostrarán tus pagos recientes.",
                        fontSize = 14.sp,
                        color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                    )
                }
            } else {
                // Mensaje por defecto antes de confirmar
                Text(
                    text = "Ingresa el Folio en el recuadro para saber más sobre esta información.",
                    fontSize = 14.sp,
                    color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}