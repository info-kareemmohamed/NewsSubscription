package com.example.newssubscription.onboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.newssubscription.app.navigation.Routes
import com.example.newssubscription.onboarding.presentation.OnBoardingScreenRoot

fun NavGraphBuilder.addOnBoardingGraph() {
    navigation<Routes.AppStartNavigation>(
        startDestination = Routes.OnBoardingScreen
    ) {
        composable<Routes.OnBoardingScreen> {
            OnBoardingScreenRoot()
        }
    }
}



