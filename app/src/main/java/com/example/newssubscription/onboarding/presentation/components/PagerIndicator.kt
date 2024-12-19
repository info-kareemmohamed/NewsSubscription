package com.example.newssubscription.onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.newssubscription.app.ui.Dimens.IndicatorSize
import com.example.newssubscription.app.ui.theme.BlueGray


@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pagesCount: Int,
    currentPage: Int,
    selectColor: Color = MaterialTheme.colorScheme.primary,
    unSelectColor: Color = BlueGray
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        repeat(pagesCount) { index ->
            val color = if (index == currentPage) selectColor else unSelectColor

            Box(
                modifier = Modifier
                    .size(IndicatorSize)
                    .clip(CircleShape)
                    .background(color)
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun OnboardingIndicatorPreview() {
    PagerIndicator(
        pagesCount = 3,
        currentPage = 1
    )

}