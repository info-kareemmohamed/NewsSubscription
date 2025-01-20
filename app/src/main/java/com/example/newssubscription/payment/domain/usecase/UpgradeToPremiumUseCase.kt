package com.example.newssubscription.payment.domain.usecase

import com.example.news.core.domain.model.User
import com.example.newssubscription.core.domain.repository.UserRepository
import java.time.ZonedDateTime
import java.util.Date
import javax.inject.Inject

class UpgradeToPremiumUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        user: User,
        startPremiumDate: ZonedDateTime,
        subscriptionMonths: Int
    ) {
        val newExpirationDate = startPremiumDate.plusMonths(subscriptionMonths.toLong())
        userRepository.updateUser(
            user.copy(
                expirationPremiumDate = Date.from(newExpirationDate?.toInstant()),
                premium = true,
                startPremiumDate = Date.from(startPremiumDate.toInstant()),
                subscriptionMonths = subscriptionMonths,
                hasFreeRead = false,
                startFreeReadDate = null,
                articlesShownCount = 0,
                visitedArticleUrls = emptyList()
            )
        )
    }
}