package com.example.newssubscription.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.newssubscription.alarm.service.AlarmsService
import com.example.newssubscription.notification.data.local.WelcomeNotification.Companion.WELCOME_CHANNEL_ID
import com.example.newssubscription.notification.data.remote.PushNotificationsService.Companion.REMOTE_CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // List of channels to create
        val channels = listOf(
            NotificationChannel(
                REMOTE_CHANNEL_ID,
                "New news",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Used to show new news" },
            NotificationChannel(
                AlarmsService.ALARM_CHANNEL_ID,
                "Alarm",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Used to show alarm notifications"
                setSound(null, null)
            },
            NotificationChannel(
                WELCOME_CHANNEL_ID,
                "Welcome Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Channel for welcoming new users" }
        )

        // Create all channels
        channels.forEach { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }
}