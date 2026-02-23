package com.example.krishproject1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisplayMap()
        }
    }
}

@Composable
fun DisplayMap(modifier: Modifier = Modifier) {

    val singapore = LatLng(1.35, 103.87)

    val markerState = remember { MarkerState(position = singapore) }

    var addressInfo by remember { mutableStateOf("") }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLongClick = {
            markerState.position = it
            addressInfo = "Resolving Address..."
            println("Long clicked at ${it.latitude}, ${it.longitude}")
        }
    ) {
        Marker(
            state = markerState,
            title = addressInfo,
            snippet = "${markerState.position.latitude}, ${markerState.position.longitude}"
        )
    }
}