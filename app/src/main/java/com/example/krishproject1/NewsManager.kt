package com.example.krishproject1

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject

class NewsManager {

    val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        builder.addInterceptor(loggingInterceptor)

        okHttpClient = builder.build()
    }

     fun getSources(category: String, apiKey: String): List<NewsSource> {
        // making request body
        val request = Request.Builder()
            .url("https://newsapi.org/v2/sources?category=$category&apiKey=$apiKey")
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()

        Log.d("response", "$response")

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            // make list of sources
            val sources = mutableListOf<NewsSource>()
            // create json object with response body
            val json = JSONObject(responseBody)
            // get "sources" from the json object
            val sourcesArray = json.getJSONArray("sources")
            // for each source
            for (i in 0 until sourcesArray.length()) {
                // current source
                val currentSource = sourcesArray.getJSONObject(i)
                // get id, name, and description
                val id = currentSource.getString("id")
                val name = currentSource.getString("name")
                val description = currentSource.getString("description")
                // create NewsSource object with the extracted properties
                val source = NewsSource(
                    id = id,
                    name = name,
                    description = description
                )
                // add to list
                sources.add(source)
            }
            // return list of sources to be displayed
            return sources
        } else {
            // empty list if no sources were found
            return listOf()
        }
    }
     fun getArticles(query: String, sourceId: String, apiKey: String): List<NewsArticle> {
         // Results Networking req
        // request using specific query and source
        val request = Request.Builder()
            .url("https://newsapi.org/v2/everything?q=$query&sources=$sourceId&language=en&apiKey=$apiKey")            .get()
            .build()

         // response value
        val response = okHttpClient.newCall(request).execute()
         // response body
        val responseBody = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {

            // creating list to store articles
            val articlesList = mutableListOf<NewsArticle>()
            // converting text into json object
            val json = JSONObject(responseBody)
            // getting "articles" from json object
            val articles = json.getJSONArray("articles")

            for (i in 0 until articles.length()) {
                // for each article: get the title, description, imageURL, and url
                val currentArticle = articles.getJSONObject(i)
                val title = currentArticle.optString("title")
                val description = currentArticle.optString("description")
                val imageUrl = currentArticle.optString("urlToImage")
                val url = currentArticle.optString("url")
                val source = currentArticle.getJSONObject("source")
                val sourceName = source.optString("name")

                // creating NewsArticle object with the gathered fields
                val article = NewsArticle(
                    title = title,
                    sourceName = sourceName,
                    description = description,
                    imageUrl = imageUrl,
                    url = url,
                )
                // adding new article to results list
                articlesList.add(article)
            }
            // returning list of gathered articles
            return articlesList
        }
         // empty list if nothing found
        return listOf()
    }
}