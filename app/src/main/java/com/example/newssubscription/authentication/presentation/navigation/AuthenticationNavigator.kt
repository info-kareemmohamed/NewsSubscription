package com.example.newssubscription.authentication.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.newssubscription.app.navigation.Routes
import com.example.newssubscription.authentication.presentation.login.LoginScreenRoot
import com.example.newssubscription.authentication.presentation.signup.SignUpScreenRoot

fun NavGraphBuilder.addAuthenticationGraph(navController: NavController) {

    navigation<Routes.NewsAuthentication>(
        startDestination = Routes.LoginScreen
    ) {
        composable<Routes.LoginScreen> {
            LoginScreenRoot(
                navigateToHomeScreen = { navController.navigateToHome() },
                navigateToSignUpScreen = { navController.navigate(Routes.SignUpScreen) }
            )
        }

        composable<Routes.SignUpScreen> {
            SignUpScreenRoot(
                navigateToLoginScreen = { navController.navigate(Routes.LoginScreen) },
                navigateToHomeScreen = { navController.navigateToHome() }
            )
        }
    }
}

private fun NavController.navigateToHome() {
    navigate(Routes.HomeScreen) {
        popUpTo(Routes.NewsAuthentication) { inclusive = true }
    }
}
