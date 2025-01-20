package com.example.newssubscription.payment.presentation.util

import android.content.Context
import com.example.newssubscription.R
import com.example.newssubscription.payment.domain.util.PaymentError

fun PaymentError.asUiText(context: Context): String = when (this) {
    PaymentError.NETWORK_ERROR -> context.getString(R.string.payment_network_error)
    PaymentError.TIMEOUT -> context.getString(R.string.timeout_error)
    PaymentError.BAD_REQUEST -> context.getString(R.string.bad_request_error)
    PaymentError.UNAUTHORIZED -> context.getString(R.string.unauthorized_error)
    PaymentError.FORBIDDEN -> context.getString(R.string.forbidden_error)
    PaymentError.NOT_FOUND -> context.getString(R.string.not_found_error)
    PaymentError.TOO_MANY_REQUESTS -> context.getString(R.string.too_many_requests_error)
    PaymentError.INTERNAL_SERVER_ERROR -> context.getString(R.string.internal_server_error)
    PaymentError.SERVICE_UNAVAILABLE -> context.getString(R.string.service_unavailable_error)
    PaymentError.UNKNOWN_ERROR -> context.getString(R.string.payment_unknown_error)
}
