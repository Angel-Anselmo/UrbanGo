package com.urbango.movilidad.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap // Importación para KTX
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.urbango.movilidad.R

// Función auxiliar para convertir un Vector Drawable a Bitmap, con soporte para tintado
fun vectorToBitmap(context: Context, vectorResId: Int, tintColor: Int? = null): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    tintColor?.let { vectorDrawable?.setTint(it) } // Aplicar el color de tinte si se proporciona
    vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = createBitmap(
        vectorDrawable?.intrinsicWidth ?: 24,
        vectorDrawable?.intrinsicHeight ?: 24,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable?.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current

    // Configuración inicial del mapa centrado en Aguascalientes
    val aguascalientes = LatLng(21.88234, -102.28259) // Coordenadas del centro de Aguascalientes
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(aguascalientes, 12f)
    }

    // Lista ampliada de ubicaciones de Oxxo en Aguascalientes (coordenadas aproximadas)
    val oxxoLocations = listOf(
        LatLng(21.8850, -102.2900), // Oxxo cerca del centro
        LatLng(21.8765, -102.2760), // Oxxo en zona norte
        LatLng(21.8912, -102.3050), // Oxxo en zona oeste
        LatLng(21.8700, -102.2950), // Oxxo en zona sur
        LatLng(21.8820, -102.2600), // Oxxo en zona este
        LatLng(21.8960, -102.2850), // Oxxo en zona noroeste
        LatLng(21.8600, -102.3100), // Oxxo en zona suroeste
        LatLng(21.8750, -102.2650), // Oxxo en zona noreste
        LatLng(21.8900, -102.3200), // Oxxo en zona oeste (más al oeste)
        LatLng(21.8650, -102.2800), // Oxxo en zona sur (más al sur)
        LatLng(21.9000, -102.2950), // Oxxo en zona norte (más al norte)
        LatLng(21.8800, -102.2500), // Oxxo en zona este (más al este)
        LatLng(21.8920, -102.2750), // Oxxo en zona norte-centro
        LatLng(21.8700, -102.3000), // Oxxo en zona sur-centro
        LatLng(21.8870, -102.3100)  // Oxxo en zona oeste-centro
    )

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
                            text = "Puntos de recarga de la tarjeta 'YoVoy' en Aguascalientes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color.Black,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
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
        ) {
            // Descripción


            // Mapa de Google
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapStyleOptions = if (isDarkTheme) {
                        MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark)
                    } else {
                        null
                    }
                )
            ) {
                oxxoLocations.forEach { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Oxxo - Punto de Recarga",
                        snippet = "Recarga tu tarjeta 'YoVoy' aquí",
                        icon = vectorToBitmap(context, R.drawable.ic_credit_card, Color.Red.toArgb())
                        )
                }
            }
        }
    }
}