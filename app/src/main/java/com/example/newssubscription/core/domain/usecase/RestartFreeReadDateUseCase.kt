package com.example.newssubscription.core.domain.usecase


import com.example.news.core.domain.model.User
import com.example.newssubscription.core.domain.repository.UserRepository
import com.example.newssubscription.payment.domain.repository.CurrentTime
import java.util.Date
import javax.inject.Inject

class RestartFreeReadDateUseCase @Inject constructor(
    private val currentTime: CurrentTime,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(user: User) {
        val currentDate = currentTime.getCurrentTime()
        if (user.startFreeReadDate == null || Date.from(currentDate?.toInstant()).time >= user.startFreeReadDate.time) {

            val newDate = currentDate?.plusDays(1)
            userRepository.updateUser(
                user.copy(
                    startFreeReadDate = Date.from(newDate?.toInstant()),
                    hasFreeRead = true, articlesShownCount = 0, visitedArticleUrls = emptyList()
                )
            )
        }
    }
}