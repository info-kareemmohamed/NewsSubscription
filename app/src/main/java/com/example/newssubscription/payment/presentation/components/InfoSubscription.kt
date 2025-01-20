package com.example.newssubscription.payment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme


@Composable
fun InfoSubscription(
    startDate: String,
    endDate: String,
    months: Int,
    onDismiss: () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val dialogHeight = screenHeight * 0.4f

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dialogHeight)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(25.dp, 10.dp, 25.dp, 10.dp)
                )
                .padding(10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(id = R.string.subscription),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = colorResource(id = R.color.text_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                SubscriptionDetail(
                    stringResource(id = R.string.subscription_duration),
                    "$months ${stringResource(id = R.string.months)}"
                )
                SubscriptionDetail(stringResource(id = R.string.start_date), startDate)
                SubscriptionDetail(stringResource(id = R.string.end_date), endDate)

            }
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(5.dp))
            ) {
                Text(text = stringResource(id = R.string.close), color = Color.White)
            }
        }
    }
}

@Composable
fun SubscriptionDetail(title: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$title $value",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.text_medium),
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InfoSubscriptionPreview() {
    NewsSubscriptionTheme {
        InfoSubscription(
            "01/01/2023",
            "01/01/2024",
            5,
            {}
        )
    }
}