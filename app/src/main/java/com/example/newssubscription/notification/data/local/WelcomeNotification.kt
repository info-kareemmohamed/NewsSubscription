package com.example.newssubscription.notification.data.local

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.newssubscription.R
import com.example.newssubscription.app.MainActivity

class WelcomeNotification {

    companion object {
        const val WELCOME_CHANNEL_ID = "WelcomeChannel"
        const val NOTIFICATION_ID = 1

        /**
         * Creates and shows a welcome notification for the user.
         */
        fun sendWelcomeNotification(context: Context) {

            // Create an Intent to open MainActivity when the notification is clicked
            val intent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            // Build the Notification
            val notificationBuilder = NotificationCompat.Builder(context, WELCOME_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Welcome to News Subscription!")
                .setContentText("Welcome! Stay informed with the latest news updates.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Show the Notification
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }
}