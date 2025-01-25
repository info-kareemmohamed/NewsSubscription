package com.example.newssubscription.authentication.presentation.login

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens
import com.example.newssubscription.app.ui.Dimens.MediumPadding_30
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme
import com.example.newssubscription.authentication.presentation.common.AuthClickableText
import com.example.newssubscription.authentication.presentation.common.ButtonAuthentication
import com.example.newssubscription.authentication.presentation.common.ConnectionOptions
import com.example.newssubscription.authentication.presentation.common.ErrorText
import com.example.newssubscription.authentication.presentation.common.InputField
import com.example.newssubscription.authentication.presentation.common.PasswordField
import com.example.newssubscription.authentication.presentation.common.buildCredentialRequest
import com.example.newssubscription.authentication.presentation.login.components.ForgotPasswordMessage
import com.example.newssubscription.authentication.presentation.login.components.GreetingText
import com.example.newssubscription.authentication.presentation.login.components.RememberMeAndForgetPassword
import com.example.newssubscription.authentication.presentation.login.mvi.LoginIntent
import com.example.newssubscription.authentication.presentation.login.mvi.LoginState
import com.example.newssubscription.authentication.presentation.login.mvi.LoginViewModel
import com.example.newssubscription.authentication.presentation.util.asUiText
import com.example.newssubscription.notification.data.local.WelcomeNotification
import kotlinx.coroutines.launch


@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.loginState.collectAsStateWithLifecycle()
    val loginSuccessfully = viewModel.loginSuccessfully.collectAsStateWithLifecycle(false)

    // Ask for notification permission
    SideEffect { askNotificationPermission(context, context as Activity) }

    LoginScreen(
        state.value,
        viewModel::onIntent,
        loginSuccessfully.value,
        navigateToHomeScreen,
        navigateToSignUpScreen
    )
}


@Composable
private fun LoginScreen(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    loginSuccessfully: Boolean,
    navigateToHomeScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(loginSuccessfully) {
        if (loginSuccessfully) {
            WelcomeNotification.sendWelcomeNotification(context) // Show welcome notification
            navigateToHomeScreen()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.MediumPadding_20)
    ) {
        GreetingText()

        Spacer(modifier = Modifier.height(MediumPadding_30))

        InputField(
            label = stringResource(R.string.email),
            value = state.email,
            error = state.emailErrorMessage?.asUiText(context)
        ) { onIntent(LoginIntent.EmailChanged(it)) }
        ErrorText(error = state.emailErrorMessage?.asUiText(context))

        Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding_6))

        PasswordField(
            state.password,
            state.isPasswordVisible,
            error = state.passwordErrorMessage?.asUiText(context),
            onVisibilityChange = { onIntent(LoginIntent.VisibilityChanged(it)) },
            onValueChange = {
                onIntent(LoginIntent.PasswordChanged(it))
            })
        ErrorText(error = state.passwordErrorMessage?.asUiText(context))

        Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding_15))

        RememberMeAndForgetPassword(isChecked = state.isRememberMeChecked,
            onCheckedChange = { onIntent(LoginIntent.RememberMeChanged(it)) },
            onForgetPasswordClick = { onIntent(LoginIntent.ForgotPasswordClicked) }
        )
        ErrorText(error = state.errorMessage?.asUiText(context))

        ForgotPasswordMessage(
            show = state.forgotPasswordSuccessMessage,
            message = stringResource(R.string.forgot_password_email_sent)
        )
        val coroutineScope = rememberCoroutineScope()

        ButtonAuthentication(
            stringResource(R.string.login),
            state.isLoading
        ) { onIntent(LoginIntent.LoginClicked) }

        ConnectionOptions(
            isGoogle = state.isLoadingGoogle,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            if (!(state.isLoading || state.isLoadingGoogle)) {
                coroutineScope.launch {
                    buildCredentialRequest(context)?.let { response ->
                        onIntent(LoginIntent.GoogleSignInClicked(response))
                    }
                }
            }
        }

        AuthClickableText(
            stringResource(id = R.string.dont_have_an_account),
            stringResource(R.string.sign_up)
        ) { navigateToSignUpScreen() }
    }
}

// Ask for notification permission
private fun askNotificationPermission(context: Context, activity: Activity) {
    // This is only necessary for API level >= 33 (TIRAMISU)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val hasPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreview() {
    NewsSubscriptionTheme {
        LoginScreen(
            state = LoginState(),
            onIntent = {},
            loginSuccessfully = false,
            navigateToHomeScreen = {},
            navigateToSignUpScreen = {}
        )
    }
}

