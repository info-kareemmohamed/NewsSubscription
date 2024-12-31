package com.example.newssubscription.news.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("SELECT * FROM ArticleEntity WHERE url=:url")
    suspend fun getArticle(url: String): ArticleEntity?

    @Query("UPDATE ArticleEntity SET isCached = 0 WHERE isBookMarked=1")
    suspend fun resetCachedForBookmarkedArticles()

    @Query("DELETE FROM ArticleEntity WHERE isBookMarked=0")
    suspend fun clearArticles()
}