package com.example.newssubscription.payment.data.remote

import PaymentRequest
import PaymentResponse
import com.example.newssubscription.BuildConfig.Secret_Key
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PaymentService {
    @Headers("Authorization: Token $Secret_Key")
    @POST("v1/intention/")
    suspend fun getClientSecret(@Body paymentRequest: PaymentRequest): PaymentResponse
}