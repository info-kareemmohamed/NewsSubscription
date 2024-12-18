package com.example.newssubscription.authentication.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import com.example.newssubscription.app.ui.Dimens
import com.example.newssubscription.app.ui.Dimens.ButtonHeight

@Composable
fun ButtonAuthentication(text: String, loading: Boolean = false, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.ExtraSmallPadding_15)
            .height(ButtonHeight),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        onClick = onClick
    ) {
        if (loading) {
            LoadingAnimation()
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = White,
            )
        }
    }
}