package com.urbango.movilidad.ui

import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcedureScreen( // Cambiado de CardInfoScreen a ProcedureScreen
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()

    // Estados para controlar qué secciones están expandidas
    var expandedGeneral by remember { mutableStateOf(false) }
    var expandedEstudiante by remember { mutableStateOf(false) }
    var expandedAdultoMayor by remember { mutableStateOf(false) }
    var expandedDiscapacitado by remember { mutableStateOf(false) }

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
                            text = "Trámites",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Estado de Aguascalientes | Coordinación General de Movilidad",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
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
                text = "Servicio a través del cual ciudadano puede obtener una tarjeta de recarga para poder hacer uso del transporte urbano.",
                fontSize = 14.sp,
                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            // Sección: Tarjeta General
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF424242) else Color(0xFFF5F5F5)
                )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isDarkTheme) Color(0xFF616161) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tarjeta 'YoVoy' General",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                        IconButton(onClick = { expandedGeneral = !expandedGeneral }) {
                            Icon(
                                imageVector = if (expandedGeneral) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = if (expandedGeneral) "Colapsar" else "Expandir",
                                tint = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                    }
                    if (expandedGeneral) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Ubicar un punto de venta
                            Text(
                                text = "Ubicar un Punto de Venta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "La tarjeta YoVoy se puede comprar en distintos puntos de venta autorizados, como:\n" +
                                        "- Terminales de autobuses.\n" +
                                        "- Tiendas de conveniencia.\n" +
                                        "- Farmacias.\n" +
                                        "- Universidades.\n" +
                                        "- Centros de recarga autorizados.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Acudir al punto de venta
                            Text(
                                text = "Acudir al Punto de Venta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Una vez identificado el punto de venta, dirígete al lugar en su horario de atención.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Realizar el pago
                            Text(
                                text = "Realizar el Pago",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "El costo de la tarjeta YoVoy General es de $35 pesos. Puedes pagar en efectivo en los puntos de venta autorizados.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Recibir la tarjeta y verificar su funcionamiento
                            Text(
                                text = "Recibir la Tarjeta y Verificar su Funcionamiento",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Una vez realizado el pago, te entregarán la tarjeta YoVoy. Verifica que la tarjeta esté en buen estado y lista para usarse.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Recargar saldo
                            Text(
                                text = "Recargar Saldo",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Para utilizar la tarjeta, primero debes cargarle saldo. Puedes hacer recargas en los mismos puntos de venta donde se adquiere la tarjeta o en máquinas de recarga ubicadas en diferentes puntos de la ciudad. Los montos mínimos y máximos de recarga pueden variar según el establecimiento.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Usar la tarjeta en el transporte público
                            Text(
                                text = "Usar la Tarjeta en el Transporte Público",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Al abordar un autobús del sistema YoVoy, pasa la tarjeta por el lector del validador. La tarifa correspondiente se descontará automáticamente del saldo disponible.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Sección: Tarjeta Estudiante
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF424242) else Color(0xFFF5F5F5)
                )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isDarkTheme) Color(0xFF616161) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tarjeta 'YoVoy' Estudiante",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                        IconButton(onClick = { expandedEstudiante = !expandedEstudiante }) {
                            Icon(
                                imageVector = if (expandedEstudiante) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = if (expandedEstudiante) "Colapsar" else "Expandir",
                                tint = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                    }
                    if (expandedEstudiante) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Reunir los requisitos
                            Text(
                                text = "Reunir los Requisitos",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Para solicitar la tarjeta YoVoy Estudiante, necesitas:\n" +
                                        "- CURP (Clave Única de Registro de Población).\n" +
                                        "- Identificación oficial (INE, credencial escolar o pasaporte).\n" +
                                        "- Comprobante de estudios vigente (credencial de estudiante, constancia de estudios o tira de materias con sello de la institución).\n" +
                                        "- Comprobante de domicilio (agua, luz o teléfono, no mayor a 3 meses).\n" +
                                        "- Correo electrónico y número de teléfono de contacto.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Agendar una cita
                            Text(
                                text = "Agendar una Cita",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Ingresa a la página oficial de la Coordinación General de Movilidad (CMOV) o descarga la app Aguascalientes Digital: https://www.aguascalientes.gob.mx/CMOV\n" +
                                        "Selecciona la opción Trámite de Tarjeta YoVoy Estudiante.\n" +
                                        "Llena el formulario con tus datos y agenda una cita en el módulo más cercano.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Acudir al módulo de atención
                            Text(
                                text = "Acudir al Módulo de Atención",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Asiste en la fecha y hora asignadas con tus documentos en original y copia. Los módulos suelen ubicarse en Terminales de Transporte, la CMOV o puntos autorizados.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Realizar el pago
                            Text(
                                text = "Realizar el Pago",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "El costo de la tarjeta YoVoy Estudiante es de $35 pesos. Puedes pagar en efectivo en el módulo.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Recibir tu tarjeta
                            Text(
                                text = "Recibir tu Tarjeta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Una vez validada tu documentación y realizado el pago, te entregarán la tarjeta. Verifica que esté en buen estado y con el descuento aplicado correctamente.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Recargar saldo y usar la tarjeta
                            Text(
                                text = "Recargar Saldo y Usar la Tarjeta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Recarga saldo en los puntos autorizados o en máquinas de recarga. Al abordar el transporte YoVoy, pasa la tarjeta por el lector y se aplicará automáticamente la tarifa preferencial de estudiante.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Sección: Tarjeta Adulto Mayor
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF424242) else Color(0xFFF5F5F5)
                )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isDarkTheme) Color(0xFF616161) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tarjeta 'YoVoy' Adulto Mayor",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                        IconButton(onClick = { expandedAdultoMayor = !expandedAdultoMayor }) {
                            Icon(
                                imageVector = if (expandedAdultoMayor) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = if (expandedAdultoMayor) "Colapsar" else "Expandir",
                                tint = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                    }
                    if (expandedAdultoMayor) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Reunir los requisitos
                            Text(
                                text = "Reunir los Requisitos",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Para tramitar la tarjeta YoVoy Adulto Mayor, necesitas los siguientes documentos:\n" +
                                        "- CURP (Clave Única de Registro de Población).\n" +
                                        "- Identificación oficial (INE o pasaporte vigente).\n" +
                                        "- Comprobante de domicilio (recibo de agua, luz o teléfono, no mayor a 3 meses).\n" +
                                        "- Correo electrónico y número de teléfono de contacto.\n" +
                                        "- Tener 60 años o más.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Agendar una cita
                            Text(
                                text = "Agendar una Cita",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Ingresa a la página oficial de la Coordinación General de Movilidad (CMOV) o usa la app Aguascalientes Digital: https://www.aguascalientes.gob.mx/CMOV\n" +
                                        "Selecciona la opción Trámite de Tarjeta YoVoy Adulto Mayor.\n" +
                                        "Llena el formulario con tus datos y agenda una cita en el módulo más cercano.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Acudir al módulo de atención
                            Text(
                                text = "Acudir al Módulo de Atención",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Asiste en la fecha y hora asignadas con tus documentos en original y copia. Los módulos de atención suelen estar en Terminales de Transporte, la CMOV o puntos autorizados.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Realizar el pago
                            Text(
                                text = "Realizar el Pago",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "El costo de la tarjeta YoVoy Adulto Mayor es de $35 pesos. Puedes pagar en efectivo en el módulo de atención.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Recibir tu tarjeta
                            Text(
                                text = "Recibir tu Tarjeta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Una vez validada tu documentación y realizado el pago, te entregarán la tarjeta. Verifica que la tarjeta esté en buen estado y con el descuento aplicado correctamente.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Recargar saldo y usar la tarjeta
                            Text(
                                text = "Recargar Saldo y Usar la Tarjeta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Puedes recargar saldo en los puntos autorizados o en máquinas de recarga. Al abordar el transporte YoVoy, pasa la tarjeta por el lector y se aplicará automáticamente la tarifa preferencial para adultos mayores.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Sección: Tarjeta Discapacitado
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF424242) else Color(0xFFF5F5F5)
                )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isDarkTheme) Color(0xFF616161) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tarjeta 'YoVoy' Discapacitado",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                        IconButton(onClick = { expandedDiscapacitado = !expandedDiscapacitado }) {
                            Icon(
                                imageVector = if (expandedDiscapacitado) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = if (expandedDiscapacitado) "Colapsar" else "Expandir",
                                tint = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                    }
                    if (expandedDiscapacitado) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Reunir los requisitos
                            Text(
                                text = "Reunir los Requisitos",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Para tramitar la tarjeta YoVoy Discapacidad, necesitas presentar los siguientes documentos:\n" +
                                        "- CURP (Clave Única de Registro de Población).\n" +
                                        "- Identificación oficial (INE o pasaporte vigente).\n" +
                                        "- Comprobante de domicilio (recibo de agua, luz o teléfono, no mayor a 3 meses).\n" +
                                        "- Certificado médico que acredite la discapacidad (expedido por el DIF, IMSS, ISSSTE, Salud Estatal u otra institución oficial).\n" +
                                        "- Correo electrónico y número de teléfono de contacto.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Agendar una cita
                            Text(
                                text = "Agendar una Cita",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Ingresa a la página oficial de la Coordinación General de Movilidad (CMOV) o usa la app Aguascalientes Digital: https://www.aguascalientes.gob.mx/CMOV\n" +
                                        "Selecciona la opción Trámite de Tarjeta YoVoy Discapacidad.\n" +
                                        "Llena el formulario con tus datos y agenda una cita en el módulo más cercano.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Acudir al módulo de atención
                            Text(
                                text = "Acudir al Módulo de Atención",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "Asiste en la fecha y hora asignadas con todos los documentos en original y copia. Los módulos de atención suelen estar en Terminales de Transporte, la CMOV o puntos autorizados.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Validación y entrega de la tarjeta
                            Text(
                                text = "Validación y Entrega de la Tarjeta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "El personal revisará tu documentación para asegurarse de que cumples con los requisitos. No hay costo para esta tarjeta, ya que el servicio de transporte puede ser gratuito o con tarifa preferencial para personas con discapacidad.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )

                            // Usar la tarjeta en el transporte público
                            Text(
                                text = "Usar la Tarjeta en el Transporte Público",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Text(
                                text = "En los autobuses YoVoy, pasa la tarjeta por el lector y se aplicará automáticamente el descuento o gratuidad según el caso.",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espacio al final
        }
    }
}