package com.example.newssubscription.authentication.presentation.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens
import com.example.newssubscription.app.ui.Dimens.ButtonHeight
import com.example.newssubscription.app.ui.theme.BlueGray
import com.example.newssubscription.app.ui.theme.LightGrayColor


@Composable
fun ConnectionOptions(
    modifier: Modifier = Modifier,
    isGoogle: Boolean = false,
    onConnect: () -> Unit
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.or_connect_with),
        style = MaterialTheme.typography.bodySmall,
        color = colorResource(R.color.text_medium),
    )

    Button(
        modifier = modifier
            .padding(vertical = Dimens.ExtraSmallPadding_15)
            .width(174.dp)
            .height(ButtonHeight),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGrayColor,
            contentColor = Color.Transparent
        ),
        onClick = { onConnect() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(Dimens.ExtraSmallPadding_6))
        if (!isGoogle)
            Text(
                text = stringResource(R.string.google),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = BlueGray
            )
        else
            LoadingAnimation(circleColor = BlueGray)
    }
}