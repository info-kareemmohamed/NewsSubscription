package com.example.newssubscription.alarm.di

import android.app.Application
import com.example.newssubscription.alarm.data.AlarmPreferences
import com.example.newssubscription.alarm.data.AlarmSchedulerImpl
import com.example.newssubscription.alarm.domain.repository.AlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {


    @Provides
    @Singleton
    fun provideAlarmPreferences(application: Application): AlarmPreferences =
        AlarmPreferences(application)

    @Provides
    @Singleton
    fun provideAlarmScheduler(
        application: Application,
        alarmPreferences: AlarmPreferences
    ): AlarmScheduler =
        AlarmSchedulerImpl(application, alarmPreferences)

}