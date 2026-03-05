package com.example.krishproject1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.krishproject1.ui.theme.KrishProject1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KrishProject1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Navigate()
                    }
                }
            }
        }
    }
}

// navigation controller
@Composable
fun Navigate() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        // login screen
        composable("login") {
            LogInScreen(
                onLogin = {
                    navController.navigate("home")
                }
            )
        }

        // home screen that has routes to sources, maps, top headlines
        composable("home") {
            HomeScreen(
                // sending query from home to sources
                onSourceSelected = { query ->
                    navController.navigate("sources/$query")
                },
                        onMap ={
                    navController.navigate("map")

                },
                        onTopHeadlines = {
                    navController.navigate("top")
                }
            )
        }

        composable(
            // sources accepting the query from home
            route = "sources/{query}",
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            // extracting the value of query
            val queryVal = backStackEntry.arguments?.getString("query") ?: ""
            // showing sources screen
            SourcesScreen(
                query = queryVal,
                onResult = { q, id, name ->
                    // passing query, source id, and source name to results screen
                    navController.navigate("results/$q/$id/$name")
                }
            )
        }

        // results screen
        composable(
            // accepting query, source id, and source name from sources screen
            route = "results/{query}/{sourceId}/{sourceName}",
            arguments = listOf(
                navArgument("query") { type = NavType.StringType },
                navArgument("sourceId") { type = NavType.StringType },
                navArgument("sourceName") { type = NavType.StringType }
            )
            // extracting data
        ) { data ->
            val q = data.arguments?.getString("query") ?: ""
            val id = data.arguments?.getString("sourceId") ?: ""
            val name = data.arguments?.getString("sourceName") ?: ""

            // showing results screen
            ResultsScreen(
                userQuery = q,
                sourceId = id,
                sourceName = name
            )
        }

        // map screen
        composable("map") {
            DisplayMap()
        }

        // top headlines screen
        composable("top") {
            TopHeadlinesScreen()
        }
    }
}