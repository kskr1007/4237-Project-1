
package com.example.krishproject1

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import android.content.Intent
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit

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
    val context = LocalContext.current
    // for remembering search query
    val prefs = remember { context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE) }
    var query by remember { mutableStateOf(prefs.getString("query", "") ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Android News",
            fontSize = 40.sp,
        )
        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column(
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

                val context = LocalContext.current

               // Search Button req.
                Text(
                    text = "SEARCH",
                    color = Color.Blue,
                    // Empty Search Term req: only clickable when query field is not blank
                    modifier = Modifier.clickable(enabled = query.isNotBlank()) {
                        prefs.edit {
                            putString("query", query)
                        }
                        val intent = Intent(context, SourcesActivity::class.java)
                        // Data Persistence req: save user query
                        intent.putExtra("query", query)
                        context.startActivity(intent)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("News by Location")

                Spacer(Modifier.height(8.dp))

                val context = LocalContext.current
                // Local News Button req.
                Text(
                    text = "VIEW MAP",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        val intent = Intent(context, MapActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Top Headlines", )

                Spacer(Modifier.height(8.dp))

                // Top Headlines Button req.
                Text(
                    text = "VIEW TOP HEADLINES",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        val intent = Intent(context, TopHeadlinesActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen()
}
