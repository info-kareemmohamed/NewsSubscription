package com.example.newssubscription.app.navigation

import com.example.newssubscription.news.domain.model.Article
import kotlinx.serialization.Serializable

sealed interface Routes {

    @Serializable
    data object OnBoardingScreen : Routes

    @Serializable
    data object NewsAuthentication : Routes

    @Serializable
    data object LoginScreen : Routes

    @Serializable
    data object SignUpScreen : Routes

    @Serializable
    data object NewsNavigatorScreen : Routes

    @Serializable
    data object HomeScreen : Routes

    @Serializable
    data object SearchScreen : Routes

    @Serializable
    data object BookmarkScreen : Routes

    @Serializable
    data object SettingsScreen : Routes

    @Serializable
    data class DetailsScreen(val article: Article) : Routes

    @Serializable
    data object AppStartNavigation : Routes

    @Serializable
    data object NewsNavigation : Routes
}