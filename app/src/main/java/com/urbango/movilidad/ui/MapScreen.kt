package com.urbango.movilidad.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme // Importación añadida para isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName
import com.urbango.movilidad.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onPermissionDenied: () -> Unit = {}
) {
    val context = LocalContext.current
    // Ubicacion inicial: Aguascalientes
    val initialLocation = remember { LatLng(21.8782, -102.2916) }
    var userLocation by remember { mutableStateOf(initialLocation) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 12f)
    }

    // Estado para el permiso de ubicacion
    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Lanzador para solicitar permisos
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(context, cameraPositionState) { location ->
                userLocation = location
            }
        } else {
            onPermissionDenied()
        }
    }

    // Solicitar permiso al iniciar si no lo tenemos
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getCurrentLocation(context, cameraPositionState) { location ->
                userLocation = location
            }
        }
    }

    // Estado para las polilineas de las rutas (ida y regreso)
    var polylinePointsOutward by remember { mutableStateOf(listOf<LatLng>()) } // Ida
    var polylinePointsReturn by remember { mutableStateOf(listOf<LatLng>()) }  // Regreso

    // Estado para el submenú
    var expanded by remember { mutableStateOf(false) }
    var selectedRoute by remember { mutableStateOf("Selecciona una ruta") }
    // Lista de rutas con nombres simplificados
    val routes = listOf("Ruta 1", "Ruta 2", "Ruta 3")

    // Determinar si estamos en modo oscuro usando isSystemInDarkTheme()
    val isDarkTheme = isSystemInDarkTheme()

    // UI del mapa
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            // Barra superior con el nombre de la ruta
            TopAppBar(
                title = {
                    Text(
                        text = selectedRoute,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
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
                .padding(innerPadding) // Ajusta el mapa para que no se superponga con la barra superior e inferior
        ) {
            // Submenú
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isDarkTheme) Color.DarkGray.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                // Título
                Text(
                    text = "Selecciona tu ruta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Botón del submenú con ícono
                Button(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = selectedRoute)
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = "Seleccionar ruta",
                            tint = Color.White
                        )
                    }
                }

                // Menú desplegable
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isDarkTheme) Color.DarkGray else Color.White)
                ) {
                    routes.forEach { route ->
                        DropdownMenuItem(
                            text = { Text(text = route, color = if (isDarkTheme) Color.White else Color.Black) },
                            onClick = {
                                selectedRoute = route
                                expanded = false
                                fetchRoute(context, route) { outwardPoints, returnPoints ->
                                    polylinePointsOutward = outwardPoints
                                    polylinePointsReturn = returnPoints
                                }
                            }
                        )
                    }
                }
            }

            // Mapa con polilíneas, flechas y marcadores
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission,
                    mapType = if (isDarkTheme) MapType.NORMAL else MapType.NORMAL,
                    mapStyleOptions = if (isDarkTheme) MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark) else null
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                )
            ) {
                // Polilínea de ida (rojo) con flechas y marcadores
                if (polylinePointsOutward.isNotEmpty()) {
                    Polyline(
                        points = polylinePointsOutward,
                        color = Color.Red, // Sentido de ida
                        width = 8f
                    )
                    // Añadir flechas a lo largo de la ruta de ida
                    DrawArrowsOnRoute(points = polylinePointsOutward, color = Color.Red, zoomLevel = cameraPositionState.position.zoom)

                    // Marcadores de inicio y fin (ida)
                    Marker(
                        state = MarkerState(position = polylinePointsOutward.first()),
                        title = "Inicio (Ida)",
                        snippet = "Punto de inicio de la ruta de ida"
                    )
                    Marker(
                        state = MarkerState(position = polylinePointsOutward.last()),
                        title = "Fin (Ida)",
                        snippet = "Punto de fin de la ruta de ida"
                    )
                }
                // Polilínea de regreso (azul) con flechas y marcadores
                if (polylinePointsReturn.isNotEmpty()) {
                    Polyline(
                        points = polylinePointsReturn,
                        color = Color.Blue, // Sentido de regreso
                        width = 8f
                    )
                    // Añadir flechas a lo largo de la ruta de regreso
                    DrawArrowsOnRoute(points = polylinePointsReturn, color = Color.Blue, zoomLevel = cameraPositionState.position.zoom)

                    // Marcadores de inicio y fin (regreso)
                    Marker(
                        state = MarkerState(position = polylinePointsReturn.first()),
                        title = "Inicio (Regreso)",
                        snippet = "Punto de inicio de la ruta de regreso"
                    )
                    Marker(
                        state = MarkerState(position = polylinePointsReturn.last()),
                        title = "Fin (Regreso)",
                        snippet = "Punto de fin de la ruta de regreso"
                    )
                }
            }
        }
    }
}

// Función para obtener la ubicacion actual
private fun getCurrentLocation(
    context: Context,
    cameraPositionState: CameraPositionState,
    onLocationObtained: (LatLng) -> Unit
) {
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)

                    // Usar corrutina para animar la cámara
                    CoroutineScope(Dispatchers.Main).launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(userLatLng, 15f)
                            )
                        )
                    }

                    onLocationObtained(userLatLng)
                } ?: run {
                    Toast.makeText(
                        context,
                        "No se pudo obtener la ubicacion",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Error al obtener la ubicacion: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}

// Función para obtener rutas de Directions API (ida y regreso)
private fun fetchRoute(
    context: Context,
    selectedRoute: String,
    onResult: (List<LatLng>, List<LatLng>) -> Unit // Devuelve dos listas: ida y regreso
) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(DirectionsService::class.java)

    // Usamos coordenadas específicas en lugar de nombres para mayor precisión
    val (origin, destination) = when (selectedRoute) {
        "Ruta 1" -> "21.8818,-102.2916" to "21.9167,-102.2916" // Centro a Norte
        "Ruta 2" -> "21.8497,-102.2916" to "21.8818,-102.2500" // Sur a Este
        else -> "21.8818,-102.2916" to "21.9167,-102.2916" // Personalizada (mismo que Ruta 1 por ahora)
    }

    CoroutineScope(Dispatchers.IO).launch {
        try {
            // Solicitud para la ruta de ida (origin -> destination)
            val outwardResponse = service.getDirections(origin, destination, "TU_CLAVE_API_AQUÍ")
            var outwardPoints = emptyList<LatLng>()
            if (outwardResponse.isSuccessful) {
                val routes = outwardResponse.body()?.routes
                if (routes?.isNotEmpty() == true) {
                    val route = routes.first()
                    val polyline = route.overviewPolyline
                    if (polyline?.points?.isNotEmpty() == true) {
                        outwardPoints = PolyUtil.decode(polyline.points)
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, "No se encontró una polilínea para la ruta de ida", Toast.LENGTH_SHORT).show()
                        }
                        Log.e("DirectionsAPI", "overview_polyline is null or points are empty (outward). Route: $route")
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "No se encontraron rutas de ida", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("DirectionsAPI", "No routes found in outward response: ${outwardResponse.body()}")
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Error al cargar la ruta de ida: ${outwardResponse.code()}", Toast.LENGTH_SHORT).show()
                }
                Log.e("DirectionsAPI", "Error (outward): ${outwardResponse.code()} - ${outwardResponse.errorBody()?.string()}")
            }

            // Solicitud para la ruta de regreso (destination -> origin)
            val returnResponse = service.getDirections(destination, origin, "TU_CLAVE_API_AQUÍ")
            var returnPoints = emptyList<LatLng>()
            if (returnResponse.isSuccessful) {
                val routes = returnResponse.body()?.routes
                if (routes?.isNotEmpty() == true) {
                    val route = routes.first()
                    val polyline = route.overviewPolyline
                    if (polyline?.points?.isNotEmpty() == true) {
                        returnPoints = PolyUtil.decode(polyline.points)
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, "No se encontró una polilínea para la ruta de regreso", Toast.LENGTH_SHORT).show()
                        }
                        Log.e("DirectionsAPI", "overview_polyline is null or points are empty (return). Route: $route")
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "No se encontraron rutas de regreso", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("DirectionsAPI", "No routes found in return response: ${returnResponse.body()}")
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Error al cargar la ruta de regreso: ${returnResponse.code()}", Toast.LENGTH_SHORT).show()
                }
                Log.e("DirectionsAPI", "Error (return): ${returnResponse.code()} - ${returnResponse.errorBody()?.string()}")
            }

            // Devolver ambas rutas (ida y regreso)
            CoroutineScope(Dispatchers.Main).launch {
                onResult(outwardPoints, returnPoints)
            }
        } catch (e: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            Log.e("DirectionsAPI", "Exception: ${e.message}", e)
        }
    }
}

// Función para dibujar flechas a lo largo de la ruta
@Composable
fun DrawArrowsOnRoute(points: List<LatLng>, color: Color, zoomLevel: Float) {
    if (points.size < 2) return // Necesitamos al menos 2 puntos para dibujar una flecha

    // Dibujar una flecha cada 10 puntos (ajustado para menos flechas y mejor rendimiento)
    val step = 10
    for (i in 0 until points.size - 1 step step) {
        val start = points[i]
        val end = points.getOrNull(i + 1) ?: continue

        // Calcular la dirección del segmento
        val deltaLat = end.latitude - start.latitude
        val deltaLng = end.longitude - start.longitude
        val angle = atan2(deltaLng, deltaLat)

        // Tamaño de la flecha ajustado según el nivel de zoom
        val baseArrowLength = 0.0008 // Aproximadamente 80 metros (ajustado para ser más grande)
        val baseArrowWidth = 0.0005  // Ancho de la flecha (ajustado para ser más grande)
        // Escalar el tamaño de la flecha según el zoom (más grande en zoom bajo, más pequeño en zoom alto)
        val zoomFactor = 15f / zoomLevel // Ajusta según el nivel de zoom
        val arrowLength = baseArrowLength * zoomFactor
        val arrowWidth = baseArrowWidth * zoomFactor

        // Calcular los puntos del triángulo de la flecha
        val arrowTip = LatLng(
            start.latitude + arrowLength * cos(angle),
            start.longitude + arrowLength * sin(angle)
        )
        val arrowBase1 = LatLng(
            start.latitude + arrowWidth * cos(angle + Math.PI / 2),
            start.longitude + arrowWidth * sin(angle + Math.PI / 2)
        )
        val arrowBase2 = LatLng(
            start.latitude + arrowWidth * cos(angle - Math.PI / 2),
            start.longitude + arrowWidth * sin(angle - Math.PI / 2)
        )

        // Dibujar el triángulo de la flecha como una polilínea cerrada
        Polyline(
            points = listOf(arrowTip, arrowBase1, arrowBase2, arrowTip),
            color = color,
            width = 4f
        )
    }
}

// Clases de la API
interface DirectionsService {
    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): retrofit2.Response<DirectionsResponse>
}

data class DirectionsResponse(
    val routes: List<Route>,
    val status: String
)

data class Route(
    @SerializedName("overview_polyline")
    val overviewPolyline: Polyline?
)

data class Polyline(
    val points: String?
)