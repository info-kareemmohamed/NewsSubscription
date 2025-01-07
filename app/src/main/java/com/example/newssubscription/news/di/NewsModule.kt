package com.example.newssubscription.news.di

import android.app.Application
import androidx.room.Room
import com.example.newssubscription.BuildConfig
import com.example.newssubscription.news.data.local.NewsDao
import com.example.newssubscription.news.data.local.NewsDatabase
import com.example.newssubscription.news.data.local.NewsDatabase.Companion.DATABASE_NAME
import com.example.newssubscription.news.data.remote.NewsApi
import com.example.newssubscription.news.data.repository.NewsRepositoryImpl
import com.example.newssubscription.news.data.repository.PhotoRepositoryImpl
import com.example.newssubscription.news.domain.repository.NewsRepository
import com.example.newssubscription.news.domain.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NewsModule {


    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleBookMarkDatabase(application: Application): NewsDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = NewsDatabase::class.java,
            name = DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleBookMarkDao(newsDatabase: NewsDatabase) = newsDatabase.dao


    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsApi,
        newsDao: NewsDao,
        newsDatabase: NewsDatabase
    ): NewsRepository =
        NewsRepositoryImpl(newsApi, newsDatabase, newsDao)


    @Provides
    @Singleton
    fun providePhotoRepository(supabaseClient: SupabaseClient): PhotoRepository =
        PhotoRepositoryImpl(supabaseClient)



    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_Key
    ) {
        install(Storage)
    }

}