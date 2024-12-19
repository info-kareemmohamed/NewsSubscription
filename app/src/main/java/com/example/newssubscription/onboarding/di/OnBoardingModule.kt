package com.example.newssubscription.onboarding.di

import com.example.newssubscription.onboarding.data.PageData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object OnBoardingModule {
    @Provides
    @Singleton
    fun providePageData(): PageData {
        return PageData
    }
}