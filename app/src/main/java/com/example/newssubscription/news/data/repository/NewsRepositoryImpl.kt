package com.example.newssubscription.news.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.newssubscription.news.data.local.NewsDao
import com.example.newssubscription.news.data.local.NewsDatabase
import com.example.newssubscription.news.data.mappers.toArticle
import com.example.newssubscription.news.data.paging.NewsRemoteMediator
import com.example.newssubscription.news.data.paging.SearchNewsPaging
import com.example.newssubscription.news.data.remote.NewsApi
import com.example.newssubscription.news.data.remote.NewsApi.Companion.PAGE_SIZE
import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDb: NewsDatabase,
    private val dao: NewsDao
) : NewsRepository {
    override fun getNews(sources: List<String>): Flow<PagingData<Article>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        remoteMediator = NewsRemoteMediator(
            newsApi = newsApi,
            sources = sources.joinToString(separator = ","),
            newsDao = dao,
            newsDb = newsDb
        ),
        pagingSourceFactory = { dao.getArticles() }
    ).flow.map { pagingData ->
        pagingData.map { articleEntity ->
            articleEntity.toArticle()
        }
    }


    override fun searchForNews(
        sources: List<String>,
        searchQuery: String
    ): Flow<PagingData<Article>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            SearchNewsPaging(
                newsApi = newsApi,
                searchQuery = searchQuery,
                sources = sources.joinToString(separator = ",")
            )
        }
    ).flow


    override suspend fun getArticleByUrl(url: String): Article? = dao.getArticle(url)?.toArticle()

    override suspend fun bookmarkArticle(url: String) = dao.updateBookmarkStatus(url, true)

    override suspend fun unBookmarkArticle(url: String) = dao.updateBookmarkStatus(url, false)

    override fun getBookMarkedArticles(): Flow<List<Article>> =
        dao.getBookmarkedArticles().map { articles ->
            articles.map { it.toArticle() }
        }
}