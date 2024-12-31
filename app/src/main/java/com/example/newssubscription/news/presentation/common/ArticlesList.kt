package com.example.newssubscription.news.presentation.common

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_6
import com.example.newssubscription.app.ui.Dimens.MediumPadding_24
import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.presentation.util.parseErrorMessage

@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: LazyPagingItems<Article>?,
    onClick: (Article) -> Unit
) {
    articles?.let { articleList ->
        if (shouldDisplayArticleList(articles = articleList)) {

            when (articleList.loadState.refresh) { // Handle loading states
                is LoadState.Loading -> ArticlesListShimmerEffect()
                else -> LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(MediumPadding_24),
                    contentPadding = PaddingValues(all = ExtraSmallPadding_6),
                ) {
                    // Display list of articles
                    items(articleList.itemCount) { index ->
                        articleList[index]?.let { article ->
                            ArticleCard(article = article, onClick = { onClick(article) })
                        }
                    }
                    // Show shimmer effect while appending
                    if (articleList.loadState.append is LoadState.Loading) {
                        item { ArticlesListShimmerEffect() }
                    }
                }
            }
        }
    }
}

/**
 * Determines whether to display the article list or an error screen.
 * Returns `true` to show the list if there are no blocking errors, or `false` to display an error screen when no data is available.
 */
@Composable
fun shouldDisplayArticleList(
    articles: LazyPagingItems<Article>,
): Boolean {
    val loadState = articles.loadState

    val error = listOfNotNull(
        loadState.refresh as? LoadState.Error,
        loadState.prepend as? LoadState.Error,
        loadState.append as? LoadState.Error
    ).firstOrNull()

    return when {
        error != null -> {
            if (articles.itemCount > 0) {
                // Display a toast for non-HTTP exceptions, ignoring specific cases
                if (error.error !is retrofit2.HttpException) {
                    Toast.makeText(
                        LocalContext.current, parseErrorMessage(error), Toast.LENGTH_SHORT
                    ).show()
                }
                true
            } else {
                EmptyScreen(error = error)
                false
            }
        }
        else -> true
    }
}