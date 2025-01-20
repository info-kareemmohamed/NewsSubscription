package com.example.newssubscription.payment.data.repository

import BillingData
import Customer
import PaymentRequest
import com.example.newssubscription.BuildConfig.Mobile_Wallet_Payment_Method_ID
import com.example.newssubscription.BuildConfig.Online_Card_Payment_Method_ID
import com.example.newssubscription.core.domain.util.Result
import com.example.newssubscription.payment.data.remote.PaymentService
import com.example.newssubscription.payment.domain.repository.PaymentRepository
import com.example.newssubscription.payment.domain.util.PaymentError
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(private val api: PaymentService) :
    PaymentRepository {
    override suspend fun getClientSecret(
        amount: Float,
        currency: String,
        userName: String,
        userEmail: String,
        subscriptionMonths: Int
    ): Result<String, PaymentError> {
        return try {
            val paymentRequest = PaymentRequest(
                amount = amount * 100,
                currency = currency,
                payment_methods = listOf(
                    Online_Card_Payment_Method_ID,
                    Mobile_Wallet_Payment_Method_ID
                ),
                billing_data = BillingData(
                    first_name = userName,
                    last_name = userName,
                    email = userEmail,
                ),
                customer = Customer(
                    first_name = userName,
                    last_name = userName,
                    email = userEmail,
                ),
                extras = mapOf("subscriptionMonths" to subscriptionMonths.toString())
            )

            val response = api.getClientSecret(paymentRequest)
            Result.Success(response.client_secret)

        } catch (e: Exception) {
            val error = when (e) {
                is java.net.UnknownHostException -> PaymentError.NETWORK_ERROR
                is java.net.SocketTimeoutException -> PaymentError.TIMEOUT
                is retrofit2.HttpException -> {
                    when (e.code()) {
                        400 -> PaymentError.BAD_REQUEST
                        401 -> PaymentError.UNAUTHORIZED
                        403 -> PaymentError.FORBIDDEN
                        404 -> PaymentError.NOT_FOUND
                        429 -> PaymentError.TOO_MANY_REQUESTS
                        500 -> PaymentError.INTERNAL_SERVER_ERROR
                        503 -> PaymentError.SERVICE_UNAVAILABLE
                        else -> PaymentError.UNKNOWN_ERROR
                    }
                }

                else -> PaymentError.UNKNOWN_ERROR
            }
            Result.Error(error)
        }
    }
}