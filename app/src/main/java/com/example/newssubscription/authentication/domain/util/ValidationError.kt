package com.example.newssubscription.authentication.domain.util

import com.example.newssubscription.core.domain.util.Error


sealed interface ValidationError : Error {

    enum class PasswordError : ValidationError {
        PASSWORD_EMPTY,
        PASSWORD_TOO_SHORT,
        CONFIRM_PASSWORD_DOES_NOT_MATCH,
    }

    enum class EmailError : ValidationError {
        EMAIL_EMPTY,
        EMAIL_INVALID_FORMAT,
    }

    enum class UsernameError : ValidationError {
        USERNAME_EMPTY,
        USERNAME_TOO_SHORT,
        USERNAME_TOO_LONG,
        USERNAME_INVALID_FORMAT,
    }
}