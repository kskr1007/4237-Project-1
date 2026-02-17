package com.example.krishproject1



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    var query by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Android News", fontSize = 40.sp)

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Term Search")

            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search") }
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "SEARCH",
                modifier = Modifier.clickable(
                    enabled = query.isNotBlank()
                )
                { }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("News by Location")

            Spacer(Modifier.height(8.dp))

            Text(
                text = "VIEW MAP",
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Top Headlines")

            Spacer(Modifier.height(8.dp))

            Text(
                text = "VIEW TOP HEADLINES",
                modifier = Modifier.clickable { }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen()
}
