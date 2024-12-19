package com.example.newssubscription.core.di

import android.app.Application
import com.example.newssubscription.core.data.repository.LocalUserAppEntryImpl
import com.example.newssubscription.core.domain.repository.LocalUserAppEntry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserAppEntry(application: Application): LocalUserAppEntry {
        return LocalUserAppEntryImpl(application)
    }
}