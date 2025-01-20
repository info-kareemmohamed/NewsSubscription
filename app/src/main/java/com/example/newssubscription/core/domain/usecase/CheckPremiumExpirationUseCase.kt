package com.example.newssubscription.core.domain.usecase

import com.example.news.core.domain.model.User
import com.example.newssubscription.core.domain.repository.UserRepository
import com.example.newssubscription.payment.domain.repository.CurrentTime
import java.util.Date
import javax.inject.Inject

class CheckPremiumExpirationUseCase @Inject constructor(
    private val currentTime: CurrentTime,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(user: User) {
        if (!user.premium) return
        currentTime.getCurrentTime().let { currentDate ->

            if (user.expirationPremiumDate == null || Date.from(currentDate?.toInstant()).time >= user.expirationPremiumDate.time) {
                userRepository.updateUser(
                    user.copy(
                        premium = false,
                        subscriptionMonths = 0,
                        startPremiumDate = null,
                        expirationPremiumDate = null,
                        visitedArticleUrls = emptyList(),
                        articlesShownCount = 0,
                        hasFreeRead = true,
                        startFreeReadDate = Date.from(currentDate?.plusDays(1)?.toInstant())
                    )
                )
            }
        }
    }
}