package com.example.newssubscription.payment.domain.repository

import com.example.newssubscription.core.domain.util.Result
import com.example.newssubscription.payment.domain.util.PaymentError

interface PaymentRepository {
    suspend fun getClientSecret(
        amount: Float,
        currency: String = "EGP",
        userName: String,
        userEmail: String,
        subscriptionMonths: Int
    ): Result<String, PaymentError>
}