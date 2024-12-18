package com.example.newssubscription.authentication.presentation.common

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.newssubscription.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext


suspend fun buildCredentialRequest(context: Context): GetCredentialResponse? {
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(
                    BuildConfig.FIREBASE_SERVER_CLIENT_ID
                )
                .setAutoSelectEnabled(false)
                .build()
        )
        .build()

    return try {
        CredentialManager.create(context).getCredential(request = request, context = context)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        null
    }
}
