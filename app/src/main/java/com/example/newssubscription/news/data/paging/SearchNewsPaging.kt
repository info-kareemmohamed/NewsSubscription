package com.example.newssubscription.news.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newssubscription.news.data.mappers.toArticle
import com.example.newssubscription.news.data.remote.NewsApi
import com.example.newssubscription.news.domain.model.Article

class SearchNewsPaging(
    private val newsApi: NewsApi,
    private val searchQuery: String,
    private val sources: String
) : PagingSource<Int, Article>() {


    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val newsResponse =
                newsApi.searchForNews(searchQuery = searchQuery, sources = sources, page = page)

            val articles =
                newsResponse.articles.distinctBy { it.title }//to avoid duplicate articles
                    .map { it.toArticle() }

            LoadResult.Page(
                data = articles,
                nextKey = page + 1,
                prevKey = if (page == 1) null else page - 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}