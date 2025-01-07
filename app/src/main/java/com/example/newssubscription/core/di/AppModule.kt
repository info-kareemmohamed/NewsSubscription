package com.example.newssubscription.core.di

import android.app.Application
import com.example.newssubscription.core.data.repository.LocalUserAppEntryImpl
import com.example.newssubscription.core.data.repository.UserRepositoryImpl
import com.example.newssubscription.core.domain.repository.LocalUserAppEntry
import com.example.newssubscription.core.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(firebaseAuth, firestore)
    }

}