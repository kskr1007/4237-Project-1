package com.example.krishproject1

data class NewsResponse(
    val articles: List<NewsArticle>,
    val totalResults: Int
)