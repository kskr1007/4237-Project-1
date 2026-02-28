package com.example.krishproject1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import coil.compose.AsyncImage
import androidx.core.net.toUri
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // to display query and source name at top
        val userQuery = intent.getStringExtra("query") ?: ""
        val sourceId = intent.getStringExtra("sourceId") ?: ""
        val sourceName = intent.getStringExtra("sourceName") ?: ""
        setContent {
            ResultsScreen(userQuery, sourceId, sourceName)
        }
    }
}
@Composable
fun ResultsScreen(userQuery: String, sourceId: String, sourceName: String) {
    val newsManager = remember { NewsManager() }
    val apiKey = stringResource(id = R.string.NewsKey)

    var articles by remember { mutableStateOf<List<NewsArticle>>(emptyList()) }
    val context = LocalContext.current
    LaunchedEffect(userQuery, sourceId) {
        articles = withContext(Dispatchers.IO) {
            // if no source is is provided, show all sources
            if (sourceId.isEmpty()) {
                newsManager.skipSources(userQuery, apiKey)
            }
            // if a source is provided use that in request
            else {
                newsManager.getArticles(userQuery, sourceId, apiKey)
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        Text(
            // Results Title req: showing user source and query at top
            text = "$sourceName results for $userQuery",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            // Article Display req: displaying image, name, sourceName, and description for each article in loaded list from source
            items(articles) { article ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
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
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = article.sourceName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            // null safe description
                            text = article.description ?: "",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

