package com.urbango.movilidad.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()

    // Estados para los campos del formulario
    var selectedReportType by remember { mutableStateOf("") } // Inicialmente vacío para mostrar el placeholder
    var expanded by remember { mutableStateOf(false) }
    val reportTypes = listOf("Informar Fallos", "Tiempo de Espera", "Sugerir Mejoras") // Reemplazado "Reportar Incidente" por "Tiempo de Espera"

    var dateTime by remember { mutableStateOf("") }
    var route by remember { mutableStateOf("") }
    var unitNumber by remember { mutableStateOf("") }
    var waitTime by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

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
                            text = "Reportes y Quejas sobre el Servicio",
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
            // Introducción
            Text(
                text = "Ayúdanos a Mejorar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.White else Color.Black,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(
                text = "Reporta alguna inconformidad, o escribe tu opinión sobre las mejoras que podríamos hacer sobre los sistemas de movilidad.",
                fontSize = 14.sp,
                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Tipo de Reporte (Dropdown)
            Text(
                text = "TIPO DE REPORTE",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.White else Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box {
                OutlinedTextField(
                    value = if (selectedReportType.isEmpty()) "" else selectedReportType,
                    onValueChange = { /* No editable directamente */ },
                    modifier = Modifier
                        .fillMaxWidth(),
                    readOnly = true,
                    placeholder = { Text("Elije una Opción", color = Color.Gray) },
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Expandir",
                                tint = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                        unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                        focusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                        unfocusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                        focusedBorderColor = if (isDarkTheme) Color.White else Color.Black,
                        unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color.DarkGray
                    )
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isDarkTheme) Color.DarkGray else Color.White)
                ) {
                    reportTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type, color = if (isDarkTheme) Color.White else Color.Black) },
                            onClick = {
                                selectedReportType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Descripción del tipo de reporte (mostrar solo si se ha seleccionado un tipo)
            if (selectedReportType.isNotEmpty()) {
                Text(
                    text = when (selectedReportType) {
                        "Informar Fallos" -> "Bienvenido a la sección 'Informar Fallos'\n" +
                                "Aquí podrás redactar un reporte sobre un inconveniente que " +
                                "esté presente relacionado al servicio general de movilidad.\n" +
                                "(Ej. Retrasos constantes)."
                        "Tiempo de Espera" -> "Bienvenido a la sección 'Tiempo de Espera'\n" +
                                "Aquí podrás reportar el tiempo de espera aproximado para una ruta específica.\n" +
                                "Esto nos ayudará a mejorar los horarios y la puntualidad del servicio."
                        else -> "Bienvenido a la sección 'Sugerir Mejoras'\n" +
                                "Si tienes alguna sugerencia de mejora acerca del servicio de " +
                                "movilidad, puedes redactarla en este apartado.\n" +
                                "El propósito de este apartado es mejorar el servicio " +
                                "basándose en diferentes perspectivas.\n" +
                                "(Ej. Transporte, Aplicación Móvil, etc.)"
                    },
                    fontSize = 14.sp,
                    color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )
            }

            // Campos del formulario según el tipo de reporte (mostrar solo si se ha seleccionado un tipo)
            if (selectedReportType.isNotEmpty()) {
                if (selectedReportType == "Informar Fallos") {
                    // Fecha y Hora
                    Text(
                        text = "Fecha y Hora",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = dateTime,
                        onValueChange = { dateTime = it },
                        placeholder = { Text("DD/MM/AAAA, 00:00") },
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

                    // Ruta
                    Text(
                        text = "Ruta",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = route,
                        onValueChange = { route = it },
                        placeholder = { Text("00") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            focusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                            focusedBorderColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color.DarkGray
                        )
                    )

                    // Número Económico de la Unidad
                    Text(
                        text = "Número Económico de la Unidad",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = unitNumber,
                        onValueChange = { unitNumber = it },
                        placeholder = { Text("0000") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            focusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                            focusedBorderColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color.DarkGray
                        )
                    )

                    // Evidencia (Foto o Video)
                    Text(
                        text = "Evidencia (Foto o Video)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    Button(
                        onClick = { /* TODO: Implementar lógica para tomar foto o video */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = if (isDarkTheme) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Agregar Foto o Video",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("PNG, JPEG")
                        }
                    }
                } else if (selectedReportType == "Tiempo de Espera") {
                    // Ruta
                    Text(
                        text = "Ruta",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = route,
                        onValueChange = { route = it },
                        placeholder = { Text("00") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            focusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                            focusedBorderColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color.DarkGray
                        )
                    )

                    // Tiempo de Espera Aproximado
                    Text(
                        text = "Tiempo de Espera Aproximado",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = waitTime,
                        onValueChange = { waitTime = it },
                        placeholder = { Text("00:00 hrs / 00 min") },
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

                    // Ubicación de Espera
                    Text(
                        text = "Ubicación de Espera",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    Button(
                        onClick = { /* TODO: Implementar lógica para compartir ubicación */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = if (isDarkTheme) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Compartir mi Ubicación",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Compartir mi Ubicación")
                        }
                    }
                } else if (selectedReportType == "Sugerir Mejoras") {
                    // Tipo de Mejora (para "Sugerir Mejoras")
                    Text(
                        text = "Tipo de Mejora",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = { /* TODO: Implementar lógica si se necesita */ },
                        placeholder = { Text("Ej. Transporte, Aplicación Móvil, etc.") },
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
                }

                // Breve Descripción
                Text(
                    text = "Breve Descripción",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color.Black,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Texto Libre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                        unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                        focusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                        unfocusedContainerColor = if (isDarkTheme) Color.DarkGray else Color.White,
                        focusedBorderColor = if (isDarkTheme) Color.White else Color.Black,
                        unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color.DarkGray
                    )
                )

                // Botón Enviar
                Button(
                    onClick = { /* TODO: Implementar lógica para enviar el reporte */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3F51B5), // Color azul similar al de la imagen
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Enviar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}