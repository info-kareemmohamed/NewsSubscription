package com.example.newssubscription.news.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: ArticleEntity)

    @Upsert
    suspend fun upsert(articles: List<ArticleEntity>)

    @Query("SELECT * FROM ArticleEntity WHERE isBookMarked = 1")
    fun getBookmarkedArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM ArticleEntity WHERE isCached = 1")
    fun getArticles(): PagingSource<Int, ArticleEntity>

    @Query("UPDATE ArticleEntity SET isBookMarked = :isBookMarked WHERE url = :url")
    suspend fun updateBookmarkStatus(url: String, isBookMarked: Boolean)

    @Query("UPDATE ArticleEntity SET isBookMarked = NOT isBookMarked WHERE url = :url")
    suspend fun toggleBookmarkStatus(url: String)

    @Query("SELECT * FROM ArticleEntity WHERE url=:url")
    suspend fun getArticle(url: String): ArticleEntity?

    @Query("UPDATE ArticleEntity SET isCached = 0 WHERE isBookMarked=1")
    suspend fun resetCachedForBookmarkedArticles()

    @Transaction
    suspend fun toggleOrInsertBookmark(article: ArticleEntity) {
        // Check if the article already exists in the database by its URL
        val existingArticle = getArticle(article.url)

        if (existingArticle != null) {
            // If the article exists, toggle the isBookMarked value
            toggleBookmarkStatus(article.url)
        } else {
            // If the article does not exist, add it to the database with isBookMarked set to true
            upsert(article.copy(isBookMarked = true))
        }
    }

    @Query("DELETE FROM ArticleEntity WHERE isBookMarked=0")
    suspend fun clearArticles()
}