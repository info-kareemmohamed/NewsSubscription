package com.example.newssubscription.news.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.navigation.toRoute
import com.example.newssubscription.app.navigation.Routes
import com.example.newssubscription.core.domain.usecase.CanUserReadArticleUseCase
import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.presentation.bookmark.BookmarkScreenRoot
import com.example.newssubscription.news.presentation.details.DetailsScreenRoot
import com.example.newssubscription.news.presentation.home.HomeScreenRoot
import com.example.newssubscription.news.presentation.search.SearchScreenRoot
import com.example.newssubscription.news.presentation.settings.SettingsScreenRoot
import com.example.newssubscription.payment.presentation.PaymentScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf


fun NavGraphBuilder.addNewsNavigatorGraph(
    authenticationCallback: () -> Unit,
    canUserReadArticleUseCase: CanUserReadArticleUseCase
) {
    navigation<Routes.NewsNavigation>(
        startDestination = Routes.NewsNavigatorScreen
    ) {
        composable<Routes.NewsNavigatorScreen> {
            NewsNavigator(
                authenticationCallback = authenticationCallback,
                canUserReadArticleUseCase
            )
        }
    }
}


@Composable
private fun NewsNavigator(
    authenticationCallback: () -> Unit,
    canUserReadArticleUseCase: CanUserReadArticleUseCase
) {

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
    var showPaymentDialog by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()


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
                    navigateToDetails = { article ->
                        checkNavigateToDetails(
                            canUserReadArticleUseCase = canUserReadArticleUseCase,
                            article = article,
                            navController = navController,
                            showPaymentDialog = { showPaymentDialog = it },
                            coroutineScope = coroutineScope
                        )
                    })
            }

            composable<Routes.SearchScreen> {
                SearchScreenRoot(navigateToDetails = { article ->
                    checkNavigateToDetails(
                        canUserReadArticleUseCase = canUserReadArticleUseCase,
                        article = article,
                        navController = navController,
                        showPaymentDialog = { showPaymentDialog = it },
                        coroutineScope = coroutineScope
                    )
                })

            }

            composable<Routes.BookmarkScreen> {
                BookmarkScreenRoot(navigate = { article ->
                    navController.navigate(Routes.DetailsScreen(article = article))
                })
            }

            composable<Routes.SettingsScreen> {
                SettingsScreenRoot(onLogout = {
                    authenticationCallback()
                })
            }

            composable<Routes.DetailsScreen>(
                typeMap = mapOf(typeOf<Article>() to CustomNavType.ArticleType)
            ) {
                val article = it.toRoute<Routes.DetailsScreen>().article
                DetailsScreenRoot(article = article,
                    navigateUp = { navController.navigateUp() })
            }
        }
        if (showPaymentDialog) {
            PaymentScreen(onDismiss = { showPaymentDialog = false }, premium = false)
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


private fun checkNavigateToDetails(
    canUserReadArticleUseCase: CanUserReadArticleUseCase,
    article: Article,
    navController: NavController,
    showPaymentDialog: (Boolean) -> Unit,
    coroutineScope: CoroutineScope,
    articleShownCount: Int = 4
) {
    coroutineScope.launch {
        if (canUserReadArticleUseCase(article.url, articleShownCount)) {
            showPaymentDialog(false)
            navController.navigate(Routes.DetailsScreen(article = article))
        } else showPaymentDialog(true)
    }
}