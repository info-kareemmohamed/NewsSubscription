package com.example.newssubscription.news.presentation.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens.ArticleCardSize
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_3
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_6
import com.example.newssubscription.app.ui.Dimens.MediumPadding_24
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme

@Composable
fun ArticleCardShimmerEffect(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(ArticleCardSize)
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect(),
        )
        Column(
            modifier = Modifier
                .padding(horizontal = ExtraSmallPadding_3)
                .height(ArticleCardSize),
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmerEffect()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.width(ExtraSmallPadding_6))
                Box(
                    modifier = Modifier
                        .weight(0.3f)
                        .height(20.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(ExtraSmallPadding_6))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerEffect()
                )
            }
        }
    }
}

/**
 * Shimmer effect for loading articles.
 */
@Composable
fun ArticlesListShimmerEffect() {
    Column(
        verticalArrangement = Arrangement.spacedBy(MediumPadding_24),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .align(Alignment.CenterHorizontally)
                .shimmerEffect()
        )
        repeat(10) {
            ArticleCardShimmerEffect()
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "Shimmer Effect Transition")
    val alpha = transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = "Shimmer Effect Alpha Animation"
    ).value

    this.then(
        background(color = colorResource(id = R.color.shimmer).copy(alpha = alpha))
    )
}


@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ArticleCardShimmerEffectPreview() {
    NewsSubscriptionTheme {
        ArticleCardShimmerEffect()
    }
}