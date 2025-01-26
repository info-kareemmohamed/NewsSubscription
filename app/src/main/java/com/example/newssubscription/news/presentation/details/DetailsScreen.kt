package com.example.newssubscription.news.presentation.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens.ArticleImageHeight
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_10
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_15
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_3
import com.example.newssubscription.app.ui.Dimens.MediumPadding_20
import com.example.newssubscription.app.ui.Dimens.MediumPadding_24
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme
import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.presentation.details.components.DetailsTopBar
import com.example.newssubscription.news.presentation.details.mvi.DetailsIntent
import com.example.newssubscription.news.presentation.details.mvi.DetailsState
import com.example.newssubscription.news.presentation.details.mvi.DetailsViewModel


@Composable
fun DetailsScreenRoot(
    article: Article,
    viewModel: DetailsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.onIntent(DetailsIntent.LoadArticle(article)) }

    DetailsScreen(
        state = state.value,
        event = viewModel::onIntent,
        navigateUp = navigateUp
    )
}

@Composable
fun DetailsScreen(
    state: DetailsState,
    event: (DetailsIntent) -> Unit,
    navigateUp: () -> Unit,
) {

    DisposableEffect(Unit) {
        // Handle the final result of whether the item is bookmarked or not
        // and modify the bookmark state in the data layer when the composable is disposed.
        onDispose {
            event.invoke(DetailsIntent.SaveFinalBookmarkInData)
        }
    }

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        DetailsTopBar(
            isBookmarked = state.isBookmarked,
            onBrowsingClick = {
                state.article?.url?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(url)
                    }
                    context.startActivity(intent)
                }
            },
            onShareClick = {
                state.article?.url?.let { shareUrl ->
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareUrl)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share via"))
                }
            },
            onBookmarkClick = { event(DetailsIntent.OnBookmarkClick) },
            onBackClick = navigateUp
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(ArticleImageHeight)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.article?.imageUrl)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f
                        )
                    )
                    .padding(ExtraSmallPadding_15),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {

                    Text(
                        text = state.article?.title ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        textAlign = TextAlign.Justify,
                        fontWeight = FontWeight.Bold
                    )

                }
            }

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ExtraSmallPadding_10,
                    vertical = MediumPadding_24
                )
        ) {
            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = state.article?.sourceName ?: "",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(id = R.color.text_title)
                    )

                    Image(
                        modifier = Modifier.size(ExtraSmallPadding_15),
                        painter = painterResource(id = R.drawable.ic_verification),
                        contentDescription = null
                    )
                }
                Text(
                    text = state.article?.author ?: "",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(id = R.color.text_title)
                )
                Spacer(modifier = Modifier.height(ExtraSmallPadding_3))
                HorizontalDivider(thickness = 2.dp)

                Spacer(modifier = Modifier.height(MediumPadding_20))

                Text(
                    text = state.article?.content ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(id = R.color.body)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    NewsSubscriptionTheme(dynamicColor = false) {
        DetailsScreen(
            state = DetailsState(
                article = Article(
                    author = "Kareem Mohamed",
                    title = "Coinbase says Apple blocked its last app release on NFTs in Wallet ... - CryptoSaurus",
                    description = "Coinbase says Apple blocked its last app release on NFTs in Wallet ... - CryptoSaurus",
                    content = "We use cookies and data to Deliver and maintain Google services Track outages and protect against spam, fraud, and abuse Measure audience engagement and site statistics to unde… [+1131 chars]",
                    publishedAt = "2023-06-16T22:24:33Z",
                    sourceName = "BBS",
                    url = "https://consent.google.com/ml?continue=https://news.google.com/rss/articles/CBMiaWh0dHBzOi8vY3J5cHRvc2F1cnVzLnRlY2gvY29pbmJhc2Utc2F5cy1hcHBsZS1ibG9ja2VkLWl0cy1sYXN0LWFwcC1yZWxlYXNlLW9uLW5mdHMtaW4td2FsbGV0LXJldXRlcnMtY29tL9IBAA?oc%3D5&gl=FR&hl=en-US&cm=2&pc=n&src=1",
                    imageUrl = "https://media.wired.com/photos/6495d5e893ba5cd8bbdc95af/191:100/w_1280,c_limit/The-EU-Rules-Phone-Batteries-Must-Be-Replaceable-Gear-2BE6PRN.jpg",
                ),
            ),
            event = {},
            navigateUp = {}
        )
    }
}