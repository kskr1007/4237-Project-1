package com.example.krishproject1

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
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
    val context = LocalContext.current
    // shared preferences
    val prefs = remember { context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE) }
    // default lat,lon is DC
    var lat by remember { mutableStateOf(prefs.getString("lat", "38.9073") ?: "38.9073") }
    var lon by remember { mutableStateOf(prefs.getString("lon", "77.0369") ?: "77.0369") }
    // google api key
    val apiKey = stringResource(id = R.string.MapsKey)
    // used to call helper functions in news manager class
    val newsManager = remember { NewsManager() }
    // variable to store city given by geocoding api request
    var loc by remember { mutableStateOf("") }
    // **I had to search google maps and api to get this variable**
    val scope = rememberCoroutineScope()
    // on/off switch for showing results box on bottom
    var showResults by remember { mutableStateOf(false) }
    // center point using lat and lon
    val center = LatLng(lat.toDouble(), lon.toDouble())
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
    // launched effect allows the saved lat, lon to be used on the start of the activity
    LaunchedEffect(Unit) {
        // get the coordinates from local vars (that were updated with new values)
        val initialLat = lat.toDoubleOrNull() ?: 38.91
        val initialLon = lon.toDoubleOrNull() ?: -77.4

        // get city using saved coordinates
        val city = withContext(Dispatchers.IO) {
            newsManager.getCity(initialLat, initialLon, apiKey)
        }

        // show articles for saved city
        if (city.isNotEmpty()) {
            loc = city
            showResults = true
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = { it ->
                markerState.position = it
                // Data Persistence req: save location and results
                // save lat
                val savedLat = it.latitude.toString()
                // save lon
                val savedLon = it.longitude.toString()
                // add to shared pref
                prefs.edit {
                    putString("lat", savedLat)
                    putString("lon", savedLon)
                }
                // update local variables
                lat = savedLat
                lon = savedLon

                // scope.launch uses a different thread to run in background
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

        // News Overlay req: used box to organize articles
        if (showResults) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(350.dp)
                    // offset raises the base of the box so it doesn't cover zoom
                    .offset(y = (-90).dp)
                    .background(Color.White)
            ) {
                // calling results screen function to display articles over the map
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