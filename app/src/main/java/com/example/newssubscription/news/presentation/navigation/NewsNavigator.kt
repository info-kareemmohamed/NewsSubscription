package com.example.newssubscription.news.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.newssubscription.app.navigation.Routes
import com.example.newssubscription.news.presentation.bookmark.BookmarkScreenRoot
import com.example.newssubscription.news.presentation.home.HomeScreenRoot
import com.example.newssubscription.news.presentation.search.SearchScreenRoot


fun NavGraphBuilder.addNewsNavigatorGraph() {
    navigation<Routes.NewsNavigation>(
        startDestination = Routes.NewsNavigatorScreen
    ) {
        composable<Routes.NewsNavigatorScreen> {
            NewsNavigator()
        }
    }
}


@Composable
private fun NewsNavigator() {

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    // Determine if the bottom bar should be visible based on the current route
    val isBottomBarVisible by remember(currentDestination) {
        derivedStateOf {
            currentDestination?.hierarchy?.any { destination ->
                bottomRouteDataList.any { item ->
                    destination.hasRoute(item.route::class)
                }
            } == true
        }
    }

    // Find the current route for the bottom navigation
    val foundRoute by remember(currentDestination) {
        derivedStateOf {
            bottomRouteDataList.find { item ->
                currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                NewsBottomNavigation(
                    selectedItemRoute = foundRoute?.route ?: Routes.HomeScreen,
                    onItemClick = { route -> navigateToTab(navController, route.route) }
                )
            }
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()

        NavHost(
            navController = navController,
            startDestination = Routes.HomeScreen,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            composable<Routes.HomeScreen> {
                HomeScreenRoot(navigateToSearch = {
                    navigateToTab(navController, Routes.SearchScreen)
                },
                    navigateToDetails = { articleUrl -> })
            }
            composable<Routes.SearchScreen> {
                SearchScreenRoot(navigateToDetails = { articleUrl -> })
            }

            composable<Routes.BookmarkScreen> {
                BookmarkScreenRoot(navigate = { articleUrl -> })
            }

            composable<Routes.SettingsScreen> { Box(Modifier.fillMaxSize()) }
        }
    }
}

private fun navigateToTab(navController: NavController, route: Routes) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) { saveState = true }
        }
        restoreState = true
        launchSingleTop = true
    }
}




