package com.example.newssubscription.payment.di

import com.example.newssubscription.BuildConfig.PAYMENT_BASE_URL
import com.example.newssubscription.payment.data.remote.PaymentService
import com.example.newssubscription.payment.data.repository.CurrentTimeImpl
import com.example.newssubscription.payment.data.repository.PaymentRepositoryImpl
import com.example.newssubscription.payment.domain.repository.CurrentTime
import com.example.newssubscription.payment.domain.repository.PaymentRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentModule {

    @Provides
    @Singleton
    fun providePaymentApi(): PaymentService {
        return Retrofit.Builder()
            .baseUrl(PAYMENT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentService::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrentTimeRepository(firestore: FirebaseFirestore): CurrentTime {
        return CurrentTimeImpl(firestore)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(api: PaymentService): PaymentRepository {
        return PaymentRepositoryImpl(api)
    }

}