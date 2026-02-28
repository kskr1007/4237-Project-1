
package com.example.krishproject1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SourcesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Search Term req: the query from the home screen
        val userQuery = intent.getStringExtra("query") ?: ""
        setContent {
            SourcesScreen(userQuery)
        }
    }
}

@Composable
fun SourcesScreen(userQuery: String) {
    // for the news api
    val newsManager = remember { NewsManager() }
    // getting api key from values folder
    val apiKey = stringResource(id = R.string.NewsKey)
    // start with business
    var selectedCategory by remember { mutableStateOf("business") }
    // list of sources
    var sources by remember { mutableStateOf<List<NewsSource>>(emptyList()) }

    LaunchedEffect(selectedCategory) {
        sources = withContext(Dispatchers.IO) {
            // Changing Sources req: based on selected category, api request is different
            newsManager.getSources(
                selectedCategory,
                apiKey
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Search Term req
        Text(
            text = "Results for $userQuery",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // categories dropdown section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Category",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // calling function that displays the dropdown
            SourcesCategoryDropdown(
                onCategorySelected = { category ->
                    selectedCategory = category
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // sources section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Sources",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        // Sources Networking req: lazy column to show card for each source, no paging here.
        LazyColumn(
            // constrains the space the source results take
            modifier = Modifier.weight(1f),
        ) {
            items(sources) { source ->

                val context = LocalContext.current

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            // for passing query and source name to results screen
                            val intent = Intent(context, ResultsActivity::class.java)
                            intent.putExtra("query", userQuery)
                            intent.putExtra("sourceId", source.id)
                            intent.putExtra("sourceName", source.name)
                            context.startActivity(intent)
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = source.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = source.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Skip Sources req
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current
            Button(
                onClick = {
                    // if skip sources is clicked, send query, blank source id, and general source name
                    val intent = Intent(context, ResultsActivity::class.java)
                    intent.putExtra("query", userQuery)
                    intent.putExtra("sourceId", "")
                    intent.putExtra("sourceName", "All Sources")
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Skip Source Selection")
            }
        }
    }
}

// Sources Categories req: using box and dropdown menu
@Composable
fun SourcesCategoryDropdown(
    // Sources Categories req: fields for news api
    categories: List<String> = listOf(
        "Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"
    ),
    onCategorySelected: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Business") }

    // the dropdown box composable. Used android docs and internet to find code
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = selectedCategory,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {

            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category)},
                    onClick = {
                        selectedCategory = category
                        expanded = false
                        onCategorySelected(category)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SourcesPreview() {
    SourcesScreen("Hello World")
}
