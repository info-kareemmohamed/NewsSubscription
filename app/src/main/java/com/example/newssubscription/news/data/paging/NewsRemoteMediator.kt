package com.example.newssubscription.news.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newssubscription.news.data.local.ArticleEntity
import com.example.newssubscription.news.data.local.NewsDao
import com.example.newssubscription.news.data.local.NewsDatabase
import com.example.newssubscription.news.data.mappers.toArticleEntity
import com.example.newssubscription.news.data.remote.NewsApi
import kotlinx.coroutines.flow.first

/**
 * `NewsRemoteMediator` implements the Single Source of Truth (SSOT) pattern
 * to efficiently manage and synchronize data between a remote API and a local database.
 *
 * ## Purpose:
 * - To seamlessly integrate data fetched from the remote API with the local database.
 * - To isolate and handle different contexts (e.g., HOME, SEARCH, BOOKMARK) without overlapping cached data.
 *
 * ## Key Features:
 * - Implements a clear separation between cached articles and bookmarked articles.
 * - Ensures that bookmarked articles are preserved across refreshes and do not interfere with new cached data.
 *
 * ## Cache and Bookmark Management:
 * - Cached Articles:** Represents articles fetched from the API for the current session.
 * - Bookmarked Articles:** Represents user-saved articles that should always persist, regardless of cache updates.
 * - When clearing cached data during a `LoadType.REFRESH`:
 *   - Articles with `isBookMarked = true` are retained.
 *   - The `isCached` status of all bookmarked articles is set to `false` to prevent them from appearing with new data unless they are part of the new API response.
 * - When inserting new articles from the API:
 *   - If an article exists in the database as bookmarked, its `isCached` status is updated to `true` if it is part of the new data.
 *   - Articles that are bookmarked but not present in the new data remain in the database but are excluded from the cache.
 */

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsApi: NewsApi, // News api instance
    private val sources: String, // Sources for fetching news
    private val newsDao: NewsDao,  // Data Access Object for handling local database interactions
    private val newsDb: NewsDatabase,  // Database instance to handle transactions
) : RemoteMediator<Int, ArticleEntity>() {

    private var page = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        return try {
            // Determine the correct page to load based on load type
            page = when (loadType) {
                LoadType.REFRESH -> 1 // Reset to the first page if it's a refresh
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)  // No prepend for this API
                LoadType.APPEND -> {
                    if (state.lastItemOrNull() == null) 1
                    else page + 1
                }
            }

            // Fetch new data from the API
            val newsResponse = newsApi.getNews(sources = sources, page = page)

            // Fetch bookmarked articles
            val bookmarkedUrls = newsDao.getBookmarkedArticles().first().map { it.url }.toSet()

            // Perform all database operations in a single transaction to ensure consistency
            newsDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // Reset cached status for bookmarks to false and clear non-bookmarked articles
                    newsDao.resetCachedForBookmarkedArticles()
                    newsDao.clearArticles()
                }

                // Map API articles to database models
                val articles = newsResponse.articles.map { articleDto ->
                    articleDto.toArticleEntity(
                        isBookMarked = articleDto.url in bookmarkedUrls,
                        isCached = true
                    )
                }

                // Insert articles into the database
                newsDao.upsert(articles)
            }

            // Return success and indicate if more data is available
            MediatorResult.Success(endOfPaginationReached = newsResponse.articles.isEmpty())

        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }
}