package com.example.newssubscription.news.presentation.settings.mvi


sealed interface SettingsIntent {
    data object OnLogoutClick : SettingsIntent
    data class OnImageChange(val byteArray: ByteArray?) : SettingsIntent

}