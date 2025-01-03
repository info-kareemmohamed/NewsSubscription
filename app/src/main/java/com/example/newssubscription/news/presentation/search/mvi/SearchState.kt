package com.example.newssubscription.news.presentation.search.mvi

import androidx.paging.PagingData
import com.example.newssubscription.news.domain.model.Article
import kotlinx.coroutines.flow.Flow


data class SearchState(
    val searchQuery: String = "",
    val articles: Flow<PagingData<Article>>? = null
)