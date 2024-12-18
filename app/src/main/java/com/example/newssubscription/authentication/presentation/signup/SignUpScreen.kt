package com.example.newssubscription.authentication.presentation.signup

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_10
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_15
import com.example.newssubscription.app.ui.Dimens.MediumPadding_20
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme
import com.example.newssubscription.authentication.presentation.common.AuthClickableText
import com.example.newssubscription.authentication.presentation.common.ButtonAuthentication
import com.example.newssubscription.authentication.presentation.common.ConnectionOptions
import com.example.newssubscription.authentication.presentation.common.ErrorText
import com.example.newssubscription.authentication.presentation.common.InputField
import com.example.newssubscription.authentication.presentation.common.PasswordField
import com.example.newssubscription.authentication.presentation.common.buildCredentialRequest
import com.example.newssubscription.authentication.presentation.signup.mvi.SignUpIntent
import com.example.newssubscription.authentication.presentation.signup.mvi.SignUpState
import com.example.newssubscription.authentication.presentation.signup.mvi.SignUpViewModel
import com.example.newssubscription.authentication.presentation.util.asUiText
import kotlinx.coroutines.launch


@Composable
fun SignUpScreenRoot(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateToLoginScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit
) {
    val state = viewModel.signUpState.collectAsStateWithLifecycle()
    val signUpSuccessfully = viewModel.signUpSuccessfully.collectAsStateWithLifecycle(false)
    val googleSignUpSuccessfully =
        viewModel.googleSignUpSuccessfully.collectAsStateWithLifecycle(false)

    SignUpScreen(
        state.value,
        viewModel::onIntent,
        signUpSuccessfully.value,
        googleSignUpSuccessfully.value,
        navigateToLoginScreen,
        navigateToHomeScreen
    )
}


@Composable
private fun SignUpScreen(
    state: SignUpState,
    onIntent: (SignUpIntent) -> Unit,
    signUpSuccessfully: Boolean,
    googleSignUpSuccessfully: Boolean,
    navigateToLoginScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit = {}
) {
    val context = LocalContext.current

    LaunchedEffect(googleSignUpSuccessfully) {
        if (googleSignUpSuccessfully) navigateToHomeScreen()
    }

    LaunchedEffect(signUpSuccessfully) {
        if (signUpSuccessfully) navigateToLoginScreen()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(MediumPadding_20),
        verticalArrangement = Arrangement.spacedBy(ExtraSmallPadding_10),
    ) {
        GreetingText()

        Spacer(modifier = Modifier.height(ExtraSmallPadding_15))

        InputField(
            label = stringResource(R.string.user_name),
            value = state.username,
            state.usernameErrorMessage?.asUiText(context)
        ) { onIntent(SignUpIntent.UsernameChanged(it)) }
        ErrorText(error = state.usernameErrorMessage?.asUiText(context))


        InputField(
            label = stringResource(R.string.email),
            state.email,
            state.emailErrorMessage?.asUiText(context)
        ) {
            onIntent(SignUpIntent.EmailChanged(it))
        }
        ErrorText(error = state.emailErrorMessage?.asUiText(context))


        PasswordField(
            value = state.password,
            visibility = state.isPasswordVisible,
            error = state.passwordErrorMessage?.asUiText(context),
            onVisibilityChange = { onIntent(SignUpIntent.PasswordVisibilityChanged(it)) },
            onValueChange = { onIntent(SignUpIntent.PasswordChanged(it)) })
        ErrorText(error = state.passwordErrorMessage?.asUiText(context))


        PasswordField(
            label = stringResource(R.string.confirm_password),
            value = state.confirmPassword,
            visibility = state.isConfirmPasswordVisible,
            error = state.confirmPasswordErrorMessage?.asUiText(context),
            onVisibilityChange = {
                onIntent(SignUpIntent.ConfirmPasswordVisibilityChanged(it))
            },
            onValueChange = { onIntent(SignUpIntent.ConfirmPasswordChanged(it)) })
        ErrorText(error = state.confirmPasswordErrorMessage?.asUiText(context))


        ButtonAuthentication(stringResource(R.string.sign_up), state.isLoading) {
            onIntent(SignUpIntent.Submit)
        }
        ErrorText(error = state.errorMessage?.asUiText(context))

        val coroutineScope = rememberCoroutineScope()
        ConnectionOptions(
            isGoogle = state.isLoadingGoogle,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            if (!(state.isLoading || state.isLoadingGoogle)) {
                coroutineScope.launch {
                    buildCredentialRequest(context)?.let { response ->
                        onIntent(SignUpIntent.GoogleSignInClicked(response))
                    }
                }
            }
        }

        AuthClickableText(
            stringResource(id = R.string.already_have_an_account),
            stringResource(R.string.login)
        ) {
            navigateToLoginScreen()
        }
    }
}

@Composable
fun GreetingText() {
    Text(
        text = stringResource(R.string.hello_),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 50.sp
        ),
        color = MaterialTheme.colorScheme.primary,
    )

    Text(
        text = stringResource(R.string.sign_up_to_get_started),
        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
        color = colorResource(R.color.text_medium),
    )
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignUpScreenPreview() {
    NewsSubscriptionTheme {
        SignUpScreen(
            state = SignUpState(),
            onIntent = {},
            signUpSuccessfully = false,
            navigateToLoginScreen = {},
            googleSignUpSuccessfully = false,
            navigateToHomeScreen = {}
        )
    }
}