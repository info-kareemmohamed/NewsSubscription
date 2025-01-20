package com.example.newssubscription.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.newssubscription.app.navigation.NavGraph
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme
import com.example.newssubscription.core.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            mainViewModel.keepOnScreenCondition.value
        }
        setContent {
            NewsSubscriptionTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                        val startDestination = mainViewModel.startDestination.value
                        NavGraph(
                            startDestination = startDestination,
                            canUserReadArticleUseCase = mainViewModel.canUserReadArticleUseCase
                        )
                    }
                }
            }
        }
    }
}
