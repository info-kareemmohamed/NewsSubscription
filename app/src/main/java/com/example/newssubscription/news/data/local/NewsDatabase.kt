package com.example.newssubscription.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract val dao: NewsDao

    companion object {
        const val DATABASE_NAME = "news_database"
    }
}