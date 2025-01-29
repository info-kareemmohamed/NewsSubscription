package com.example.newssubscription.news.presentation.util

import android.content.Context
import androidx.paging.LoadState
import com.example.newssubscription.R
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun parseErrorMessage(error: LoadState.Error?, context: Context): String {
    return when (val exception = error?.error) {
        is SocketTimeoutException -> {
            context.getString(R.string.error_server_unavailable)
        }

        is ConnectException -> {
            context.getString(R.string.error_internet_unavailable)
        }

        is UnknownHostException -> {
            context.getString(R.string.error_no_internet_connection)
        }

        is HttpException -> {
            when (exception.code()) {
                426 -> context.getString(R.string.error_no_results_found)
                404 -> context.getString(R.string.error_no_results_found)
                204 -> context.getString(R.string.error_no_results_found)
                else -> context.getString(R.string.error_unknown)
            }
        }

        else -> {
            context.getString(R.string.error_unknown)
        }
    }
}