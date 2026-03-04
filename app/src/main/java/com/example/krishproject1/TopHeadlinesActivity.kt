
package com.example.krishproject1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.core.net.toUri
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TopHeadlinesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TopHeadlinesScreen()
        }
    }
}

@Composable
fun TopHeadlinesScreen() {
    // current page
    var currentPage by remember { mutableStateOf(1) }
    // count of total results
    var totalResults by remember { mutableStateOf(0) }
    // Max Pages req: max page = total results/20, ultimate max is 5.
    // Changing Top Headlines req: runs every time totalResults is changed (when category is changed)
    val maxPage = remember(totalResults) {
        // find totalResults/20 and round up
        val calculate = kotlin.math.ceil(totalResults / 20.0).toInt()
        // make sure its bounded
        // **had to use android docs to find this method**
        calculate.coerceIn(1, 5)
    }
    val context = LocalContext.current
    // for saving category selection
    val prefs = remember { context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE) }
    // for api calls
    val newsManager = remember { NewsManager() }
    // getting api key from values folder
    val apiKey = stringResource(id = R.string.NewsKey)
    // load category from prefs
    var selectedCategory by remember { mutableStateOf(prefs.getString("category", "business") ?: "business") }
    // list of top articles that will be displayed
    var topArticles by remember { mutableStateOf<List<NewsArticle>>(emptyList()) }

    LaunchedEffect(selectedCategory, currentPage) {
        // response returns a NewsResponse object that includes the top 20 articles and the total results
        val response = withContext(Dispatchers.IO) {
            newsManager.getTopHeadlines(selectedCategory, apiKey, currentPage)
        }
        // top 20 articles
        topArticles = response.articles
        // total number of hits
        totalResults = response.totalResults
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Top Headlines",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // categories dropdown section
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Headline Categories req: calling function that displays the dropdown
            SourcesCategoryDropdown(
                currentCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    // Changing Top Headlines req: reset currentPage to 1 when a new category is clicked
                    currentPage = 1
                    // Data Persistence req: save the current category
                    prefs.edit {
                        putString("category", category)
                    }
                },
            )
        }

        // Sources Networking req: lazy column to show card for each source, no paging here.
        LazyColumn(
            // constrains the space the source results take
            modifier = Modifier.weight(1f),
        ) {
            items(topArticles) { article ->
                    val context = LocalContext.current
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                // open article in browser
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    article.url.toUri()
                                )
                                context.startActivity(intent)
                            }
                    ) {
                        // Article Display req
                        Column(modifier = Modifier.padding(16.dp)) {
                            AsyncImage(
                                model = article.imageUrl,
                                contentDescription = article.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                            Text(
                                text = article.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = article.description ?: "No description available",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
            }

        }
        // Paging req
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Button States req
            Button(
                // decrement current page
                onClick = { currentPage-- },
                // only enabled while greater than 1
                enabled = currentPage > 1
            ) {
                Text("Previous")
            }

            Text(text = "Page $currentPage of $maxPage")

            Button(
                // increment current page
                onClick = { currentPage++ },
                // only enabled while less than maxPage
                enabled = currentPage < maxPage
            ) {
                Text("Next")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopHeadlinesPreview() {
    TopHeadlinesScreen()
}