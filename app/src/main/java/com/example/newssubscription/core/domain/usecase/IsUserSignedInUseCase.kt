package com.example.newssubscription.core.domain.usecase

import com.example.newssubscription.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class IsUserSignedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean = authRepository.getSignedInUser() != null

}