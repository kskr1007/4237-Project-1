package com.example.krishproject1
// data class that creates the properties for an article
data class NewsArticle(
    val title: String,
    val sourceName: String,
    val description: String?,
    val content: String?,
    val imageUrl: String,
    val url: String
)