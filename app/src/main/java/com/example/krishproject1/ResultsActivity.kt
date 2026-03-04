package com.example.krishproject1

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import coil.compose.AsyncImage
import androidx.core.net.toUri

@Composable
fun ResultsScreen(userQuery: String, sourceId: String, sourceName: String, horizontal: Boolean = false) {
    // used to call functions in news manager class
    val newsManager = remember { NewsManager() }
    // news api key
    val apiKey = stringResource(id = R.string.NewsKey)
    // on/off switch for showing loading UI element
    var isLoading by remember { mutableStateOf(false) }
    // list of articles to be displayed
    var articles by remember { mutableStateOf<List<NewsArticle>>(emptyList()) }
    val context = LocalContext.current
    LaunchedEffect(userQuery, sourceId) {
        // show loading
        isLoading = true
        articles = withContext(Dispatchers.IO) {
            // if no source is is provided, show all sources (skip sources)
            if (sourceId.isEmpty()) {
                newsManager.skipSources(userQuery, apiKey)
            }
            // if a source is provided use that in request (search with a source)
            else {
                newsManager.getArticles(userQuery, sourceId, apiKey)
            }
        }
        // turn off loading
        isLoading = false
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                // Results Title req: showing user source and query at top
                text =
                    if (sourceName != userQuery) {
                        "$sourceName Results for $userQuery"
                    }
                    // in the case that it is being called via map screen.
                    // ex: I don't want it to say Washington Results for Washington
                    else {
                        "Results for $userQuery"
                    },
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // if the article is displayed over map, use row
            if (horizontal) {
                // using lazy row for horizontal scrolling
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    items(articles) { article ->
                        Card(
                            modifier = Modifier
                                .width(300.dp)
                                .clickable {
                                    // open article in browser
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        article.url.toUri()
                                    )
                                    context.startActivity(intent)
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                // if theres no image, text comes first
                                if (article.imageUrl.isNotEmpty() && article.imageUrl != "null") {
                                    AsyncImage(
                                        model = article.imageUrl,
                                        contentDescription = article.title,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .padding(bottom = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                // Title
                                Text(
                                    text = article.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                // Source
                                Text(
                                    text = article.sourceName,
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // content snippet
                                Text(
                                    text = article.content ?: "No additional content available.",
                                    style = MaterialTheme.typography.bodySmall,
                                    // just show a little content
                                    maxLines = 4,
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Description
                                Text(
                                    text = article.description ?: "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            // regular articles display
            if (!horizontal) {
                LazyColumn {
                    // Article Display req: displaying image, name, sourceName, and description for each article in loaded list from source
                    items(articles) { article ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .width(300.dp)
                                .clickable {
                                    // when card is clicked the url is used to open the article in a web browser
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        article.url.toUri()
                                    )
                                    context.startActivity(intent)
                                }
                        ) {
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
                                    // bolding
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = article.sourceName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    // null safe description
                                    text = article.description ?: "No description available",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        // if loading is true, show a spinning UI element
        // **I got this code from internet**
        // due to adding this UI feature, I had to wrap the column in a box due to alignment issues
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}