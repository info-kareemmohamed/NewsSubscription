package com.example.newssubscription.authentication.presentation.login.mvi

import com.example.newssubscription.authentication.domain.util.AuthError
import com.example.newssubscription.authentication.domain.util.ValidationError

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val forgotPasswordSuccessMessage: Boolean = false,
    val isRememberMeChecked: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingGoogle: Boolean = false,
    val errorMessage: AuthError? = null,
    val emailErrorMessage:  ValidationError.EmailError? = null,
    val passwordErrorMessage: ValidationError.PasswordError? = null
)
