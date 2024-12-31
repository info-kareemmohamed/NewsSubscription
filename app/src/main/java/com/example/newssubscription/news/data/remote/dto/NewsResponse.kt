package com.example.newssubscription.news.data.remote.dto


data class NewsResponse(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
)