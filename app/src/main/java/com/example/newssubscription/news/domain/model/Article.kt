package com.example.newssubscription.news.domain.model


data class Article(
    val author: String?,
    val content: String,
    val description: String,
    val publishedAt: String,
    val sourceName: String,
    val title: String,
    val url: String,
    val imageUrl: String,
)
