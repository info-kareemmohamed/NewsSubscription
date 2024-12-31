package com.example.newssubscription.news.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing an article in the local database.
 * - The `isBookMarked` flag is used to save articles explicitly marked by the user,
 *   separating them from articles managed as cached data.
 * - The `isCached` flag helps identify new remote data cached locally, ensuring a clear distinction
 *   between newly cached articles and user-bookmarked articles when retrieving data from Room.
 *   Without this flag, it would be difficult to manage and display the correct set of articles.
 * - This entity supports bookmarking and caching to improve offline access.
 */

@Entity
data class ArticleEntity(
    val author: String?,
    val content: String,
    val description: String,
    val publishedAt: String,
    val sourceName: String,
    val title: String,
    @PrimaryKey(autoGenerate = false)
    val url: String,
    val imageUrl: String,
    val isBookMarked: Boolean = false,
    val isCached: Boolean = false // Identifies if the article is cached
)