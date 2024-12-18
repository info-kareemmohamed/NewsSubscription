package com.example.newssubscription.authentication.presentation.util

import android.content.Context
import com.example.newssubscription.R
import com.example.newssubscription.authentication.domain.util.AuthError
import com.example.newssubscription.authentication.domain.util.ValidationError
import com.example.newssubscription.authentication.domain.util.ValidationError.EmailError
import com.example.newssubscription.authentication.domain.util.ValidationError.PasswordError
import com.example.newssubscription.authentication.domain.util.ValidationError.UsernameError

fun AuthError.asUiText(context: Context): String = when (this) {
    AuthError.NETWORK_ERROR -> context.getString(R.string.network_error)
    AuthError.INVALID_CREDENTIALS -> context.getString(R.string.invalid_credentials)
    AuthError.USER_DISABLED -> context.getString(R.string.user_disabled)
    AuthError.EMAIL_ALREADY_IN_USE -> context.getString(R.string.email_already_in_use)
    AuthError.USER_NOT_FOUND -> context.getString(R.string.user_not_found)
    AuthError.UNKNOWN_ERROR -> context.getString(R.string.unknown_error)
    AuthError.EMAIL_ALREADY_EXISTS -> context.getString(R.string.email_already_exists)
    AuthError.INTERNAL_ERROR -> context.getString(R.string.internal_error)
    AuthError.INSUFFICIENT_PERMISSION -> context.getString(R.string.insufficient_permission)
    AuthError.INVALID_ARGUMENT -> context.getString(R.string.invalid_argument)
    AuthError.EMAIL_NOT_FOUND -> context.getString(R.string.email_not_found)
}


fun ValidationError.asUiText(context: Context): String = when (this) {
    PasswordError.PASSWORD_EMPTY -> context.getString(R.string.password_cannot_be_empty)
    PasswordError.PASSWORD_TOO_SHORT -> context.getString(R.string.password_must_be_at_least_6_characters)
    UsernameError.USERNAME_EMPTY -> context.getString(R.string.username_cant_be_empty)
    UsernameError.USERNAME_TOO_SHORT -> context.getString(R.string.username_should_be_at_least_3_characters)
    UsernameError.USERNAME_TOO_LONG -> context.getString(R.string.username_should_not_exceed_20_characters)
    UsernameError.USERNAME_INVALID_FORMAT -> context.getString(R.string.username_should_only_contain_letters_and_spaces)
    EmailError.EMAIL_EMPTY -> context.getString(R.string.email_cannot_be_empty)
    EmailError.EMAIL_INVALID_FORMAT -> context.getString(R.string.invalid_email_format)
    PasswordError.CONFIRM_PASSWORD_DOES_NOT_MATCH -> context.getString(R.string.confirm_password_do_not_match)
}