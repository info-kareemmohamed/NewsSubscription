package com.example.newssubscription.alarm.presentation.mvi

sealed class AlarmIntent {
    object LoadAlarm : AlarmIntent()
    data class OnTimeChange(val hour: Int, val minute: Int) : AlarmIntent()
    data class OnDayChange(val day: Int) : AlarmIntent()
    object OnToggle : AlarmIntent()
    data class ScheduleAlarm(val hour: Int, val minute: Int, val day: Int) : AlarmIntent()
}