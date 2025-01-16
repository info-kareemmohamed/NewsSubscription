package com.example.newssubscription.alarm.presentation.mvi

import com.example.newssubscription.alarm.domain.model.Alarm

data class AlarmState(
    val alarm: Alarm? = null,
    val isAlarmEnabled: Boolean = false,
    val loading: Boolean = false
)
