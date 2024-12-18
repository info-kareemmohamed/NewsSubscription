package com.example.newssubscription.authentication.domain.repository

import androidx.credentials.GetCredentialResponse
import com.example.news.core.domain.model.User
import com.example.newssubscription.authentication.domain.util.AuthError
import com.example.newssubscription.core.domain.util.Result

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<User, AuthError>
    suspend fun signUp(username: String, email: String, password: String): Result<User, AuthError>
    suspend fun forgotPassword(email: String): Result<Unit, AuthError>
    suspend fun googleSignIn(result: GetCredentialResponse): Result<User, AuthError>
    suspend fun getSignedInUser(): User?
    fun signOut()
}