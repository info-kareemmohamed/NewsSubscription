package com.example.newssubscription.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.newssubscription.authentication.presentation.navigation.addAuthenticationGraph
import com.example.newssubscription.core.presentation.MainViewModel
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

        navigation<Routes.NewsNavigation>(
            startDestination = Routes.HomeScreen
        ) {
            composable<Routes.HomeScreen> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Button(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        onClick = { viewModel.signOut() }
                    ) { Text("Sign out") }
                }
            }
        }
    }
}