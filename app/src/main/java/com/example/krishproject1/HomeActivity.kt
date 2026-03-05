
package com.example.krishproject1

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onSourceSelected: (query:String) -> Unit, onMap: () -> Unit, onTopHeadlines: () -> Unit) {
    val context = LocalContext.current
    // for remembering search query
    val prefs = remember { context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE) }
    var query by remember { mutableStateOf(prefs.getString("query", "") ?: "") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // light purple background
            .background(Color(0xFFE1BEE7))
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Android News",
                fontSize = 40.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(40.dp))

            Card(
                // for light blue cards
                // **got from android docs**
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF64B5F6)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Term Search")

                    // Search Term Input req.
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search") },
                        leadingIcon = {
                            // code for search icon- from online search
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search Icon"
                            )
                        }
                    )

                    Spacer(Modifier.height(8.dp))

                    // Search Button req.
                    Text(
                        text = "SEARCH",
                        color = Color.Blue,
                        // Empty Search Term req: only clickable when query field is not blank
                        modifier = Modifier.clickable(enabled = query.isNotBlank()) {
                            prefs.edit {
                                putString("query", query)
                            }
                            // Data Persistence req: save user query
                            // navigate to sources
                            onSourceSelected(query)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF64B5F6)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("News by Location")

                    Spacer(Modifier.height(8.dp))

                    // Local News Button req.
                    Text(
                        text = "VIEW MAP",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            // navigate to map
                            onMap()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF64B5F6)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Top Headlines",)

                    Spacer(Modifier.height(8.dp))

                    // Top Headlines Button req.
                    Text(
                        text = "VIEW TOP HEADLINES",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            // navigate to top headlines
                            onTopHeadlines()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    //HomeScreen()
}
