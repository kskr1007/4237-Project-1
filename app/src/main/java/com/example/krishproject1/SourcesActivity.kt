
package com.example.krishproject1

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

    // list of fake sources for check-in
    val fakeSources = listOf(
        "FOX",
        "Stock News",
        "ESPN"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // header
        Text(
            text = "Results for $userQuery",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // categories dropdown section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Categories",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // calling function that displays the dropdown
            SourcesCategoryDropdown()
        }

        Spacer(modifier = Modifier.height(32.dp))

        // sources section with pages results
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Sources",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // displaying fake data using a lazy column
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(fakeSources) { source ->
                //  calling function for displaying sources using cards
                SourceRow(source)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("SKIP(SEARCH ALL SOURCES)", modifier = Modifier.clickable { })
        }
    }
}

// in the future this will use news api, for now fake data
@Composable
fun SourceRow(name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = name,
            modifier = Modifier
                .padding(16.dp),
        )
    }
}

// dropdown logic
@Composable
fun SourcesCategoryDropdown(
    // Sources Categories req: fields for news api
    categories: List<String> = listOf(
        "business", "entertainment", "general", "health", "science", "sports", "technology"
    ),
    onCategorySelected: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Select a category") }

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
