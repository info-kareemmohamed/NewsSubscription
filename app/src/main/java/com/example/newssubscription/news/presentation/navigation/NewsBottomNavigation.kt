package com.example.newssubscription.news.presentation.navigation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newssubscription.R
import com.example.newssubscription.app.navigation.Routes
import com.example.newssubscription.app.ui.Dimens.IconSize
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme


@Composable
fun NewsBottomNavigation(
    items: List<BottomRouteData> = bottomRouteDataList,
    selectedItemRoute: Routes,
    onItemClick: (BottomRouteData) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        tonalElevation = 10.dp
    ) {
        items.forEach { item: BottomRouteData ->
            NavigationBarItem(
                selected = selectedItemRoute == item.route,
                onClick = {
                    onItemClick(item)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier.size(IconSize),
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = colorResource(id = R.color.body),
                    indicatorColor = MaterialTheme.colorScheme.background
                ),
            )
        }
    }
}


@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewsBottomNavigationPreview() {
    NewsSubscriptionTheme(dynamicColor = false) {
        NewsBottomNavigation(selectedItemRoute = Routes.HomeScreen, onItemClick = {})
    }
}