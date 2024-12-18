package com.example.newssubscription.authentication.presentation.login.mvi

import androidx.credentials.GetCredentialResponse

sealed class LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent()
    data class PasswordChanged(val password: String) : LoginIntent()
    data class RememberMeChanged(val isChecked: Boolean) : LoginIntent()
    data class VisibilityChanged(val isVisible: Boolean) : LoginIntent()
    data class GoogleSignInClicked(val response: GetCredentialResponse) : LoginIntent()
    data object ForgotPasswordClicked : LoginIntent()
    data object LoginClicked : LoginIntent()
}