package com.example.newssubscription.news.domain.usecase

import com.example.newssubscription.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository

) {
    operator fun invoke() = authRepository.signOut()
}