package com.example.newssubscription.onboarding.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens.MediumPadding_24
import com.example.newssubscription.app.ui.Dimens.MediumPadding_30
import com.example.newssubscription.app.ui.Dimens.SmallPadding
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme
import com.example.newssubscription.onboarding.data.Page


@Composable
fun OnBoardingPage(
    modifier: Modifier = Modifier,
    page: Page
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val columnHeight = screenHeight * 0.8f
    val imageHeight = screenHeight * 0.5f


    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(columnHeight)
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight),
            painter = painterResource(id = page.image),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(MediumPadding_24))

        Text(
            text = page.title,
            modifier = Modifier.padding(horizontal = MediumPadding_30),
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.SemiBold),
            color = colorResource(id = R.color.display_small)
        )

        Spacer(modifier = Modifier.height(SmallPadding))

        Text(
            text = page.description,
            modifier = Modifier.padding(horizontal = MediumPadding_30),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(id = R.color.text_medium)

        )


    }


}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable

fun OnboardingPagePreview() {
    NewsSubscriptionTheme {
        OnBoardingPage(
            page = Page(
                title = "Welcome to Our App",
                description = "Explore the features of the app with ease.",
                image = R.drawable.onboarding1
            )
        )
    }
}