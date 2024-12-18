package com.example.newssubscription.authentication.presentation.signup.mvi

import com.example.newssubscription.authentication.domain.util.AuthError
import com.example.newssubscription.authentication.domain.util.ValidationError

data class SignUpState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingGoogle: Boolean = false,
    val errorMessage: AuthError? = null,
    val emailErrorMessage: ValidationError.EmailError? = null,
    val passwordErrorMessage: ValidationError.PasswordError? = null,
    val confirmPasswordErrorMessage: ValidationError.PasswordError? = null,
    val usernameErrorMessage: ValidationError.UsernameError? = null

)