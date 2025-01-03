package com.example.newssubscription.news.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.news.search.presentation.mvi.SearchViewModel
import com.example.newssubscription.app.ui.Dimens.MediumPadding_24
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme
import com.example.newssubscription.news.presentation.common.ArticlesList
import com.example.newssubscription.news.presentation.common.SearchBar
import com.example.newssubscription.news.presentation.search.mvi.SearchIntent
import com.example.newssubscription.news.presentation.search.mvi.SearchState


@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel = hiltViewModel(),
    navigateToDetails: (String) -> Unit
) {
    SearchScreen(
        state = viewModel.state,
        event = viewModel::onIntent,
        navigateToDetails = navigateToDetails
    )
}

@Composable
private fun SearchScreen(
    state: SearchState,
    event: (SearchIntent) -> Unit,
    navigateToDetails: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MediumPadding_24)
            .statusBarsPadding()
    ) {
        SearchBar(
            text = state.searchQuery,
            readOnly = false,
            onValueChange = { event(SearchIntent.UpdateSearchQuery(it)) },
            onSearch = { event(SearchIntent.SearchNews) }
        )
        Spacer(modifier = Modifier.height(MediumPadding_24))

        state.articles?.let { articles ->
            ArticlesList(articles = articles.collectAsLazyPagingItems()) { article ->
                navigateToDetails(article.url)
            }
        }
    }
}


@Preview
@Composable
fun SearchScreenPreview() {
    NewsSubscriptionTheme {
        SearchScreen(
            state = SearchState(),
            event = {},
            navigateToDetails = {}
        )
    }
}




