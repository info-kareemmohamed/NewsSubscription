package com.example.newssubscription.news.presentation.details.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun DetailsTopBar(
    isBookmarked: Boolean = false,
    onBrowsingClick: () -> Unit,
    onShareClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onBackClick: () -> Unit
) {

    TopAppBar(
        title = { },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent,
            actionIconContentColor = colorResource(id = R.color.body),
            navigationIconContentColor = colorResource(id = R.color.body)
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_arrow),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    painter = if (isBookmarked) painterResource(R.drawable.ic_bookmark_true)
                    else painterResource(R.drawable.ic_bookmark_false),
                    contentDescription = null
                )
            }
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )
            }
            IconButton(onClick = onBrowsingClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_network),
                    contentDescription = null
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailsTopBarPreview() {
    NewsSubscriptionTheme{
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            DetailsTopBar(
                onBrowsingClick = {},
                onShareClick = {},
                onBookmarkClick = {},
                onBackClick = {}
            )
        }
    }
}