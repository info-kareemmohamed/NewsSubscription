package com.example.newssubscription.news.presentation.details.mvi

import com.example.newssubscription.news.domain.model.Article


data class DetailsState (
    val article: Article? =  null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isBookmarked: Boolean = false

)