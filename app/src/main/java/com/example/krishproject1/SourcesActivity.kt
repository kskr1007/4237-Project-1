
package com.example.krishproject1

import android.content.Context
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
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun SourcesScreen(query: String, onResult: (String, String, String) -> Unit) {
    val context = LocalContext.current
    // for saving selected category
    val prefs = remember { context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE) }
    // for the news api functions
    val newsManager = remember { NewsManager() }
    // news api key
    val apiKey = stringResource(id = R.string.NewsKey)
    // remember prev category
    var selectedCategory by remember { mutableStateOf(prefs.getString("category", "business") ?: "business") }
    // list of sources that will be displayed
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
            text = "Results for $query ",
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
                // current category is the selected category
                currentCategory = selectedCategory,
                // category is the selected category
                onCategorySelected = { category ->
                    // Data Persistence req
                    selectedCategory = category
                    prefs.edit {
                        putString("category", category)
                    }
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

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            // navigate to results
                            // sending query, source id, and source name
                            onResult(query, source.id, source.name)
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
            Button(
                onClick = {
                    // navigate to results
                    // sending just the query since there's no specific source
                    onResult(query, "", "")
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
    // **Used android docs and internet to find code for dropdown**
    categories: List<String> = listOf(
        "Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"
    ),
    currentCategory: String,
    onCategorySelected: (String) -> Unit = {}
) {
    // to open/close the dropdown menu
    var expanded by remember { mutableStateOf(false) }

    // the dropdown box composable.
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = currentCategory,
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
    //SourcesScreen("Hello World")
}