package com.example.newssubscription.news.presentation.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_6
import com.example.newssubscription.app.ui.Dimens.MediumPadding_24
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme
import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.presentation.common.ArticleCard


@Composable
fun BookmarkScreenRoot(
    viewModel: BookmarkViewModel = hiltViewModel(),
    navigate: (article: Article) -> Unit
) {
    val state = viewModel.articles.collectAsStateWithLifecycle()
    BookmarkScreen(articles = state.value, navigate = navigate)
}


@Composable
private fun BookmarkScreen(
    articles: List<Article>,
    navigate: (article: Article) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(MediumPadding_24)
    ) {
        Text(
            text = "Bookmark",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = colorResource(R.color.text_title)
        )

        Spacer(modifier = Modifier.height(MediumPadding_24))

        ArticlesList(
            articles = articles,
            onClick = { article -> navigate(article) },
        )
    }

}


@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    onClick: (Article) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MediumPadding_24),
        contentPadding = PaddingValues(all = ExtraSmallPadding_6),
    ) {
        items(articles.size) { index ->
            articles[index].let { article ->
                ArticleCard(article = article, onClick = { onClick(article) })
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun BookmarkScreenPreview() {
    NewsSubscriptionTheme {
        BookmarkScreen(
            articles = listOf(
                Article(
                    "The Associated Press",
                    "TEHRAN, Iran -- A suicide bomber killed a local police officer and wounded another in a southern Iranian port city, home to a large Sunni Muslim community, local media said Sunday.nThe hard-line Jav… [+648 chars]",
                    "Iranian media say a suicide bomber killed a local police officer and wounded another in a southern Iranian port city, home to a large Sunni Muslim community",
                    "2024-12-29T07:14:56Z",
                    "BBC News",
                    "Suicide bomber kills a police officer and wounds another in southern Iran",
                    "url.com",
                    "",
                ),
                Article(
                    "The Associated Press",
                    "TEHRAN, Iran -- A suicide bomber killed a local police officer and wounded another in a southern Iranian port city, home to a large Sunni Muslim community, local media said Sunday.nThe hard-line Jav… [+648 chars]",
                    "Iranian media say a suicide bomber killed a local police officer and wounded another in a southern Iranian port city, home to a large Sunni Muslim community",
                    "2024-12-29T07:14:56Z",
                    "BBC News",
                    "Suicide bomber kills a police officer and wounds another in southern Iran",
                    "url.com",
                    "",
                )
            ),
            navigate = {}
        )
    }
}
