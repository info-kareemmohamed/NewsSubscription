package com.example.newssubscription.alarm.domain.model


data class Alarm(
    val id: Int,
    val dayOfWeek: Int,
    val hour: Int,
    val minute: Int
)