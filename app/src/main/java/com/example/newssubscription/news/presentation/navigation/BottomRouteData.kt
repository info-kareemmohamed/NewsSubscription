package com.example.newssubscription.news.presentation.navigation

import com.example.newssubscription.R
import com.example.newssubscription.app.navigation.Routes

data class BottomRouteData(
    val route: Routes,
    val icon: Int,
)

val bottomRouteDataList = listOf(
    BottomRouteData(Routes.HomeScreen, R.drawable.ic_home),
    BottomRouteData(Routes.SearchScreen, R.drawable.ic_search),
    BottomRouteData(Routes.BookmarkScreen, R.drawable.ic_bookmark_false),
    BottomRouteData(Routes.SettingsScreen, R.drawable.ic_settings),
)