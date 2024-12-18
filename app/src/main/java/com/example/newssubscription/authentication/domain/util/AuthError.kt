package com.example.newssubscription.authentication.domain.util

import com.example.newssubscription.core.domain.util.Error

enum class AuthError : Error {
    NETWORK_ERROR,
    INVALID_CREDENTIALS,
    USER_DISABLED,
    EMAIL_ALREADY_IN_USE,
    EMAIL_NOT_FOUND,
    USER_NOT_FOUND,
    EMAIL_ALREADY_EXISTS,
    INTERNAL_ERROR,
    INSUFFICIENT_PERMISSION,
    INVALID_ARGUMENT,
    UNKNOWN_ERROR;
}