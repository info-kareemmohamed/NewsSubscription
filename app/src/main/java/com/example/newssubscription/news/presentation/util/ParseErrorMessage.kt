package com.example.newssubscription.news.presentation.util

import androidx.paging.LoadState
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun parseErrorMessage(error: LoadState.Error?): String {
    return when (error?.error) {
        is SocketTimeoutException -> {
            "Server Unavailable."
        }

        is ConnectException -> {
            "Internet Unavailable."
        }

        is UnknownHostException -> {
            "No internet connection. Please check your connection and try again"
        }

        else -> {
            "Unknown Error."
        }
    }
}