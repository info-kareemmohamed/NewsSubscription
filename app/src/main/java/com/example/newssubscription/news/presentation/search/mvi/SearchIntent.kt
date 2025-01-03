package com.example.newssubscription.news.presentation.search.mvi

sealed class SearchIntent {
    data class UpdateSearchQuery(val searchQuery: String) : SearchIntent()
    data object SearchNews : SearchIntent()
}