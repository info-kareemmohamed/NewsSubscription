package com.example.newssubscription.news.domain.repository

import androidx.paging.PagingData
import com.example.newssubscription.news.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(sources: List<String>): Flow<PagingData<Article>>

    fun getBookMarkedArticles(): Flow<List<Article>>

    fun searchForNews(sources: List<String>, searchQuery: String): Flow<PagingData<Article>>

    suspend fun getArticleByUrl(url: String): Article?

    suspend fun bookmarkArticle(url: String)

    suspend fun unBookmarkArticle(url: String)
}