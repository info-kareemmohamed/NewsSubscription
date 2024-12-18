package com.example.newssubscription.authentication.presentation.signup.mvi

import androidx.credentials.GetCredentialResponse

sealed class SignUpIntent {
    data class UsernameChanged(val username: String) : SignUpIntent()
    data class EmailChanged(val email: String) : SignUpIntent()
    data class PasswordChanged(val password: String) : SignUpIntent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpIntent()
    data class PasswordVisibilityChanged(val isVisible: Boolean) : SignUpIntent()
    data class ConfirmPasswordVisibilityChanged(val isVisible: Boolean) : SignUpIntent()
    data class GoogleSignInClicked(val response: GetCredentialResponse) : SignUpIntent()
    data object Submit : SignUpIntent()

}