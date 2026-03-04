package com.example.krishproject1

// data class for news response. Includes the list of articles
// ... and the total article count
// ... used for paging calculations
data class NewsResponse(
    val articles: List<NewsArticle>,
    val totalResults: Int
)