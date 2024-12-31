package com.example.newssubscription.news.presentation.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens.ArticleCardSize
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_3
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_6
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme
import com.example.newssubscription.news.domain.model.Article


@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    article: Article,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {

        AsyncImage(
            modifier = Modifier
                .size(ArticleCardSize)
                .clip(MaterialTheme.shapes.medium),
            model = ImageRequest.Builder(LocalContext.current).data(article.imageUrl).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(horizontal = ExtraSmallPadding_3)
                .height(ArticleCardSize),
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Text(
                text = article.title,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.text_title),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.sourceName,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorResource(id = R.color.body)
                )
                Spacer(modifier = Modifier.width(ExtraSmallPadding_6))
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    tint = colorResource(id = R.color.body), contentDescription = null
                )
                Spacer(modifier = Modifier.width(ExtraSmallPadding_6))

                Text(
                    text = article.publishedAt,
                    style = MaterialTheme.typography.labelMedium,
                    color = colorResource(id = R.color.body)
                )

            }


        }


    }


}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ArticleCardPreview() {
    NewsSubscriptionTheme(dynamicColor = false) {
        ArticleCard(
            article = Article(
                author = "",
                content = "",
                description = "",
                publishedAt = "2023-07-23T10:05:41z",
                sourceName = "BBC News",
                title = "Her train broke down. Her phone died. And then she met her Saver in a",
                url = "",
                imageUrl = "https://ichef.bbci.co.uk/live-experience/cps/624/cpsprodpb/11787/production/_124395517_bbcbreakingnewsgraphic.jpg",

                )
        ) {}
    }
}