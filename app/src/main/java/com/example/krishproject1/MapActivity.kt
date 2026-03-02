package com.example.krishproject1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// For this class I used the YelpManager from class as a reference
class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisplayMap()
        }
    }
}
// Google Maps req
@Composable
fun DisplayMap(modifier: Modifier = Modifier) {
    val apiKey = stringResource(id = R.string.MapsKey)
    val newsManager = remember { NewsManager() }
    var loc by remember { mutableStateOf("") }
    // I had to search google maps and api to get this variable
    val scope = rememberCoroutineScope()
    // for showing results box on bottom
    var showResults by remember { mutableStateOf(false) }
    // center point
    val center = LatLng(38.91, -77.4)
    // for marker
    val markerState = remember { MarkerState(position = center) }
    // address
    var addressInfo by remember { mutableStateOf("") }
    // for zooming
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center, 10f)
    }
    // Location Selection req
    // Google Maps req
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = { it ->
                markerState.position = it
                scope.launch {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 15f))
                    // using Google geocoding api to get city from lat, lng
                    val city = withContext(Dispatchers.IO) {
                        newsManager.getCity(it.latitude, it.longitude, apiKey)
                    }
                    loc = city
                    // switch used to display articles box
                    showResults = true
                }
            }
        ) {
            Marker(
                state = markerState,
                title = addressInfo,
                snippet = "${markerState.position.latitude}, ${markerState.position.longitude}"
            )
        }

        // used box to organize articles
        if (showResults) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(350.dp)
                    // this raises the base of the box so it doesn't cover zoom
                    .offset(y = (-90).dp)
                    .background(Color.White)
            ) {
                // calling results screen function to display articles
                ResultsScreen(
                    userQuery = loc,
                    sourceId = "",
                    sourceName = loc,
                    horizontal = true
                )
            }
        }
    }
}