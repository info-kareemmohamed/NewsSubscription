package com.example.newssubscription.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.newssubscription.authentication.presentation.navigation.addAuthenticationGraph
import com.example.newssubscription.core.presentation.MainViewModel
import com.example.newssubscription.news.presentation.navigation.addNewsNavigatorGraph
import com.example.newssubscription.onboarding.presentation.navigation.addOnBoardingGraph

@Composable
fun NavGraph(startDestination: Routes, viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        addOnBoardingGraph()

        addAuthenticationGraph(navController)

        addNewsNavigatorGraph()
    }
}

