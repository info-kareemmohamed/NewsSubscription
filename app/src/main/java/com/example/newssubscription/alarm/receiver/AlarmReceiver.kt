package com.example.newssubscription.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.newssubscription.alarm.domain.repository.AlarmScheduler
import com.example.newssubscription.alarm.service.AlarmActions
import com.example.newssubscription.alarm.service.AlarmsService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> alarmScheduler.rescheduleAlarmIfExists()
            "com.alarmmanager" -> intent.getIntExtra("ALARM_ID", -1).takeIf { it != -1 }?.let {
                context?.startForegroundService(
                    Intent(context, AlarmsService::class.java).apply {
                        action = AlarmActions.Activate.toString()
                    }
                )
            }
        }
    }
}
