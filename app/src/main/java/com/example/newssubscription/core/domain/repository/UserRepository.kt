package com.example.newssubscription.core.domain.repository

import com.example.news.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun updateUser(user: User?)
    fun getAuthenticatedUserAsFlow(): Flow<User?>
    fun isUserSignedIn(): Boolean
}