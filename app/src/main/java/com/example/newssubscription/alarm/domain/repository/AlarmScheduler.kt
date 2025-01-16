package com.example.newssubscription.alarm.domain.repository

import com.example.newssubscription.alarm.domain.model.Alarm

interface AlarmScheduler {
    suspend fun schedule(alarm: Alarm)
    fun rescheduleAlarmIfExists()
    suspend fun cancel(alarmId: Int)
    suspend fun getAlarm(): Alarm?
}