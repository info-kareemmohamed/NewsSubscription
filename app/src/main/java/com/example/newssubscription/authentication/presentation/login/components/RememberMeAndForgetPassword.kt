package com.example.newssubscription.authentication.presentation.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens


@Composable
fun RememberMeAndForgetPassword(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onForgetPasswordClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier.size(30.dp),
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onBackground,
                    checkmarkColor = White
                )
            )
            Spacer(modifier = Modifier.width(Dimens.ExtraSmallPadding_3))
            Text(
                text = stringResource(R.string.remember_me),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_medium),
            )
        }
        Text(
            modifier = Modifier.clickable { onForgetPasswordClick() },
            text = stringResource(R.string.forgot_the_password),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
