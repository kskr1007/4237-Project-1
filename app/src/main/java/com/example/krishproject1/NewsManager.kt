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

    suspend fun retrieveSources(
        category: String,
        apiKey: String
    ): List<NewsSource> {

        // making request body
        val request = Request.Builder()
            .url("https://newsapi.org/v2/sources?category=$category&apiKey=$apiKey")
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()

        Log.d("response", "$response")

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {

            val sources = mutableListOf<NewsSource>()
            val json = JSONObject(responseBody)
            val sourcesArray = json.getJSONArray("sources")

            for (i in 0 until sourcesArray.length()) {

                val currentSource = sourcesArray.getJSONObject(i)

                val id = currentSource.getString("id")
                val name = currentSource.getString("name")
                val description = currentSource.getString("description")

                val source = NewsSource(
                    id = id,
                    name = name,
                    description = description
                )
                sources.add(source)
            }
            return sources
        } else {
            return listOf()
        }
    }
}