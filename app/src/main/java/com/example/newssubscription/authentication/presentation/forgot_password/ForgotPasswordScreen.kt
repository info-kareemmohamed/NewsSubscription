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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newssubscription.R
import com.example.newssubscription.authentication.presentation.common.ButtonAuthentication
import com.example.newssubscription.authentication.presentation.common.ErrorText
import com.example.newssubscription.authentication.presentation.common.InputField
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_15
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_6
import com.example.newssubscription.app.ui.Dimens.IconSize
import com.example.newssubscription.app.ui.Dimens.MediumPadding_20
import com.example.newssubscription.app.ui.Dimens.MediumPadding_24
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme

@Composable
fun ForgotPasswordScreen(
    email: String,
    successMessage: String?,
    errorMessage: String?,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    forgotPassword: () -> Unit,
    onBackClick: () -> Unit,
    navigateToLoginScreen: () -> Unit
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
            text = "Forgot\nPassword ?",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
            color = colorResource(id = R.color.text_medium)
        )
        Spacer(modifier = Modifier.height(ExtraSmallPadding_6))

        Text(
            text = "Don’t worry! it happens. Please enter the\naddress associated with your account.",
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(id = R.color.text_medium)
        )

        Spacer(modifier = Modifier.height(ExtraSmallPadding_15))

        InputField(error = errorMessage, label = "Email", value = email) {
            onEmailChange(it)
        }
        ErrorText(error = errorMessage)
        if (successMessage != null)
            Text(
                text = successMessage ?: "", color = Color.Green,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        Spacer(modifier = Modifier.weight(1f))
        ButtonAuthentication(
            loading = isLoading,
            text = if (successMessage == null) "Submit" else "Go To LogIn",
            onClick = {
                if (successMessage != null)
                    navigateToLoginScreen()
                else
                    forgotPassword()

            }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    NewsSubscriptionTheme {
        ForgotPasswordScreen(
            email = "",
            successMessage = null,
            errorMessage = null,
            isLoading = false,
            forgotPassword = {},
            onEmailChange = {},
            onBackClick = {},
            navigateToLoginScreen = {}
        )
    }
}
