package com.example.newssubscription.news.presentation.settings.mvi

import com.example.news.core.domain.model.User


data class SettingsState(
    val user: User? = null,
    val imageLoading: Boolean = false,
    val error: String? = null
)
