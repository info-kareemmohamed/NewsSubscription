package com.example.newssubscription.news.presentation.details.mvi

import com.example.newssubscription.news.domain.model.Article

sealed class DetailsIntent {
    data class LoadArticle(val article: Article) : DetailsIntent()
    data object OnBookmarkClick : DetailsIntent()
    data object SaveFinalBookmarkInData : DetailsIntent()
}