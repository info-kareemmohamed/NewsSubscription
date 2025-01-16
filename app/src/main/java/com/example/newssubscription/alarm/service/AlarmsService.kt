package com.example.newssubscription.alarm.service


import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.newssubscription.R
import com.example.newssubscription.app.MainActivity
import androidx.core.app.NotificationCompat



class AlarmsService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (AlarmActions.valueOf(action)) {
                AlarmActions.Activate -> activate()
                AlarmActions.Deactivate -> deactivateAlarm()
            }
        }


        return START_STICKY
    }


    private fun activate() {
        // Start playing sound
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }

        // Intent to stop the alarm service
        val stopIntent = Intent(this, AlarmsService::class.java).apply {
            action = AlarmActions.Deactivate.toString()
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
             PendingIntent.FLAG_IMMUTABLE
        )

        // Intent to open MainActivity
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            1,
            openAppIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Notification with two actions: Stop and Open App
        val notification: Notification = NotificationCompat.Builder(this, ALARM_CHANNEL_ID)
            .setContentTitle("Time to Read!")
            .setContentText("Discover something new today. Tap to open the app.")
            .setSmallIcon(R.drawable.logo) // Replace with your icon
            .addAction(R.drawable.ic_notification, "Stop Alarm", stopPendingIntent) // Stop alarm action
            .setContentIntent(openAppPendingIntent) // Open app when clicking the notification
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()

        // Start foreground service
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun deactivateAlarm() {
        // Stop sound and stop the service
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onBind(intent: Intent): IBinder? = null


    companion object {
        const val ALARM_CHANNEL_ID = "alarm_channel"
        const val NOTIFICATION_ID = 1001
    }

}

enum class AlarmActions {
    Activate,
    Deactivate
}
