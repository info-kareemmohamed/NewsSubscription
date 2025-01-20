package com.example.newssubscription.payment.domain.util

import com.example.newssubscription.core.domain.util.Error

enum class PaymentError : Error {
    NETWORK_ERROR,
    TIMEOUT,
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    TOO_MANY_REQUESTS,
    INTERNAL_SERVER_ERROR,
    SERVICE_UNAVAILABLE,
    UNKNOWN_ERROR
}