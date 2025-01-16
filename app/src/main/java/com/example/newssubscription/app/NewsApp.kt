package com.example.newssubscription.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.newssubscription.alarm.service.AlarmsService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            AlarmsService.ALARM_CHANNEL_ID,
            "Alarm",
            NotificationManager.IMPORTANCE_HIGH,
        )
        channel.description = "Used to show alarm notifications"
        channel.setSound(null, null)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}