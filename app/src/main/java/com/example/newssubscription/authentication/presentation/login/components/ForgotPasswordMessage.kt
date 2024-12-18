package com.example.newssubscription.authentication.presentation.login.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.theme.Green

@Composable
fun ForgotPasswordMessage(
    modifier: Modifier = Modifier,
    show: Boolean = false,
    message: String
) {
    if (show) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                tint = Green
            )
            Text(
                text = message,
                color = Green,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}