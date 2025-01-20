package com.example.newssubscription.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.newssubscription.authentication.presentation.navigation.addAuthenticationGraph
import com.example.newssubscription.core.domain.usecase.CanUserReadArticleUseCase
import com.example.newssubscription.news.presentation.navigation.addNewsNavigatorGraph
import com.example.newssubscription.onboarding.presentation.navigation.addOnBoardingGraph

@Composable
fun NavGraph(startDestination: Routes, canUserReadArticleUseCase: CanUserReadArticleUseCase) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        addOnBoardingGraph()

        addAuthenticationGraph(navController)

        addNewsNavigatorGraph(
            canUserReadArticleUseCase = canUserReadArticleUseCase,
            authenticationCallback = {
                navController.navigate(Routes.LoginScreen) {
                    popUpTo(Routes.NewsNavigation) { inclusive = true }
                }
            }
        )
    }
}

