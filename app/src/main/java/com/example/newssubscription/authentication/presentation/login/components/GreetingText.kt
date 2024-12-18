package com.example.newssubscription.authentication.presentation.login.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens

@Composable
fun GreetingText() {
    Text(
        text = stringResource(R.string.hello),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 50.sp
        ),
        color = colorResource(id = R.color.text_title),
    )
    Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding_6))
    Text(
        text = stringResource(R.string.again),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 50.sp
        ),
        color = MaterialTheme.colorScheme.primary,
    )
    Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding_6))
    Text(
        text = stringResource(R.string.welcome_back_youve_been_missed),
        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
        color = colorResource(R.color.text_medium),
    )
}