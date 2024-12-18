package com.example.newssubscription.authentication.presentation.forgot_password

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.newssubscription.R
import com.example.newssubscription.authentication.presentation.common.ButtonAuthentication
import com.example.newssubscription.authentication.presentation.common.PasswordField
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_6
import com.example.newssubscription.app.ui.Dimens.IconSize
import com.example.newssubscription.app.ui.Dimens.MediumPadding_20
import com.example.newssubscription.app.ui.Dimens.MediumPadding_24
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme


@Composable
fun ResetPassword(
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MediumPadding_24),
    ) {


        Icon(
            modifier = Modifier
                .size(IconSize)
                .clickable { onBackClick() },
            painter = painterResource(R.drawable.ic_back_arrow),
            tint = colorResource(id = R.color.text_medium),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(MediumPadding_20))

        Text(
            text = "Reset\nPassword",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
            color = colorResource(id = R.color.text_medium)
        )
        Spacer(modifier = Modifier.height(ExtraSmallPadding_6))

        PasswordField(value = "", visibility = false, onVisibilityChange = {}) {

        }
        Spacer(modifier = Modifier.height(ExtraSmallPadding_6))


        PasswordField(
            label = "Confirm Password",
            value = "",
            visibility = false,
            onVisibilityChange = {}) {

        }

        Spacer(modifier = Modifier.weight(1f))
        ButtonAuthentication(
            text = "Submit",
            onClick = {}
        )


    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordPreview() {
    NewsSubscriptionTheme {
        ResetPassword({}, {})
    }

}