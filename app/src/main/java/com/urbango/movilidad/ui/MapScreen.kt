package com.urbango.movilidad.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition // Import corregido
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName // Añadido para @SerializedName

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onPermissionDenied: () -> Unit = {}
) {
    val context = LocalContext.current
    // Ubicación inicial: Aguascalientes
    val initialLocation = remember { LatLng(21.8782, -102.2916) }
    var userLocation by remember { mutableStateOf(initialLocation) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 12f)
    }

    // Estado para el permiso de ubicación
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

    // Estado para las polilíneas de las rutas
    var polylinePoints by remember { mutableStateOf(listOf<LatLng>()) }

    // Estado para el submenú
    var expanded by remember { mutableStateOf(false) }
    var selectedRoute by remember { mutableStateOf("Selecciona una ruta") }
    val routes = listOf("Ruta 1: Centro - Norte", "Ruta 2: Sur - Este", "Ruta 3: Personalizada")

    // UI del mapa
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()) {
            // Submenú con Button y DropdownMenu
            Button(
                onClick = { expanded = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = selectedRoute)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                routes.forEach { route ->
                    DropdownMenuItem(
                        text = { Text(text = route) },
                        onClick = {
                            selectedRoute = route
                            expanded = false
                            fetchRoute(context, route) { points ->
                                polylinePoints = points
                            }
                        }
                    )
                }
            }

            // Mapa existente con polilínea añadida
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission, // Activa el marcador nativo
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true // Activa el botón de ubicación
                )
            ) {
                // Eliminamos el Marker personalizado para usar solo el nativo
                // El marcador de ubicación actual se muestra automáticamente
                // gracias a isMyLocationEnabled = true

                // Añado la polilínea para las rutas
                if (polylinePoints.isNotEmpty()) {
                    Polyline(
                        points = polylinePoints,
                        color = androidx.compose.ui.graphics.Color.Blue,
                        width = 5f
                    )
                }
            }
        }
    }
}

// Función para obtener la ubicación actual
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
                        "No se pudo obtener la ubicación",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Error al obtener la ubicación: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}

// Función para obtener rutas de Directions API (reescrita con mejor manejo de errores)
private fun fetchRoute(context: Context, selectedRoute: String, onResult: (List<LatLng>) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(DirectionsService::class.java)

    // Usamos coordenadas específicas en lugar de nombres para mayor precisión
    val (origin, destination) = when (selectedRoute) {
        "Ruta 1: Centro - Norte" -> "21.8818,-102.2916" to "21.9167,-102.2916" // Centro a Norte
        "Ruta 2: Sur - Este" -> "21.8497,-102.2916" to "21.8818,-102.2500" // Sur a Este
        else -> "21.8818,-102.2916" to "21.9167,-102.2916" // Personalizada (mismo que Ruta 1 por ahora)
    }

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = service.getDirections(origin, destination, "AIzaSyB0Mjjo0RlbKLWrDEM27_CRfWsCMYg67KI")
            if (response.isSuccessful) {
                val routes = response.body()?.routes
                if (routes?.isNotEmpty() == true) {
                    val route = routes.first()
                    val polyline = route.overviewPolyline
                    if (polyline?.points?.isNotEmpty() == true) { // Simplificado con isNotEmpty()
                        val points = PolyUtil.decode(polyline.points)
                        CoroutineScope(Dispatchers.Main).launch {
                            onResult(points)
                        }
                    } else {
                        // Mostrar mensaje de error en el hilo principal
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, "No se encontró una polilínea en la ruta", Toast.LENGTH_SHORT).show()
                        }
                        Log.e("DirectionsAPI", "overview_polyline is null or points are empty. Route: $route")
                    }
                } else {
                    // Mostrar mensaje de error en el hilo principal
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "No se encontraron rutas", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("DirectionsAPI", "No routes found in response: ${response.body()}")
                }
            } else {
                // Mostrar mensaje de error en el hilo principal
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Error al cargar la ruta: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
                Log.e("DirectionsAPI", "Error: ${response.code()} - ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            // Mostrar mensaje de error en el hilo principal
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            Log.e("DirectionsAPI", "Exception: ${e.message}", e)
        }
    }
}

// Clases de la API (ajustadas para coincidir con la respuesta real de la API)
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
    val status: String // Añadido para depuración
)

data class Route(
    // La API usa "overview_polyline" (con guion bajo), ajustamos el nombre del campo
    @SerializedName("overview_polyline")
    val overviewPolyline: Polyline?
)

data class Polyline(
    val points: String?
)