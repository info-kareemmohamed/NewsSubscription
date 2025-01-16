package com.example.newssubscription.alarm.data

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.example.newssubscription.alarm.domain.model.Alarm
import com.example.newssubscription.alarm.domain.repository.AlarmScheduler
import com.example.newssubscription.alarm.receiver.AlarmReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    private val context: Application,
    private val alarmPreferences: AlarmPreferences
) : AlarmScheduler {

    private val alarmManager: AlarmManager? = context.getSystemService(AlarmManager::class.java)

    override suspend fun schedule(alarm: Alarm) {
        val calendar = getNextAlarmTime(alarm)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            Intent(context, AlarmReceiver::class.java).apply {
                action = "com.alarmmanager"
                putExtra("ALARM_ID", alarm.id)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis, AlarmManager.INTERVAL_DAY * 7, // Repeat weekly
            pendingIntent
        )
        alarmPreferences.saveAlarm(alarm)

    }

    override fun rescheduleAlarmIfExists() {
        CoroutineScope(Dispatchers.IO).launch {
            alarmPreferences.readAlarm()?.let { schedule(it) }
        }
    }


    override suspend fun cancel(alarmId: Int) {
        alarmManager?.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmId,
                Intent(context, AlarmReceiver::class.java).apply {
                    action = "com.alarmmanager"
                    putExtra("ALARM_ID", alarmId)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        alarmPreferences.clearAlarm()
    }


    override suspend fun getAlarm(): Alarm? = alarmPreferences.readAlarm()


    // Helper function to calculate the next alarm time
    private fun getNextAlarmTime(alarm: Alarm): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, alarm.dayOfWeek)
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }
    }

}