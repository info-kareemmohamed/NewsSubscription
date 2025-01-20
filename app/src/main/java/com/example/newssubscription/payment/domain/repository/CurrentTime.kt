package com.example.newssubscription.payment.domain.repository

import java.time.ZonedDateTime

interface CurrentTime {
    suspend fun getCurrentTime(): ZonedDateTime?
}