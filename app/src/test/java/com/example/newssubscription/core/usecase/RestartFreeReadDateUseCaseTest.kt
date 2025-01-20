package com.example.newssubscription.core.usecase

import com.example.news.core.domain.model.User
import com.example.newssubscription.core.domain.repository.UserRepository
import com.example.newssubscription.core.domain.usecase.RestartFreeReadDateUseCase
import com.example.newssubscription.payment.domain.repository.CurrentTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

class RestartFreeReadDateUseCaseTest {

    private val mockCurrentTime: CurrentTime = mockk()
    private val mockUserRepository: UserRepository = mockk(relaxed = true)
    private val restartFreeReadDateUseCase = RestartFreeReadDateUseCase(mockCurrentTime, mockUserRepository)

    @Test
    fun `Given a user without a free read start date, When invoke is called, Then update startFreeReadDate and reset state`() =
        runTest {
            // Given
            val currentDateTime = ZonedDateTime.now(ZoneId.systemDefault())
            coEvery { mockCurrentTime.getCurrentTime() } returns currentDateTime
            val userWithoutStartFreeReadDate = User(
                premium = false,
                expirationPremiumDate = null,
                subscriptionMonths = 0,
                startPremiumDate = null,
                visitedArticleUrls = listOf("https://example.com/article1"),
                articlesShownCount = 3,
                hasFreeRead = false,
                startFreeReadDate = null
            )

            // When
            restartFreeReadDateUseCase(userWithoutStartFreeReadDate)

            // Then
            coVerify {
                mockUserRepository.updateUser(
                    userWithoutStartFreeReadDate.copy(
                        startFreeReadDate = Date.from(currentDateTime.plusDays(1).toInstant()),
                        hasFreeRead = true,
                        articlesShownCount = 0,
                        visitedArticleUrls = emptyList()
                    )
                )
            }
        }

    @Test
    fun `Given a user with an expired free read date, When invoke is called, Then update startFreeReadDate and reset state`() =
        runTest {
            // Given
            val currentDateTime = ZonedDateTime.now(ZoneId.systemDefault())
            coEvery { mockCurrentTime.getCurrentTime() } returns currentDateTime
            val expiredFreeReadUser = User(
                premium = false,
                expirationPremiumDate = null,
                subscriptionMonths = 0,
                startPremiumDate = null,
                visitedArticleUrls = listOf("https://example.com/article1"),
                articlesShownCount = 3,
                hasFreeRead = true,
                startFreeReadDate = Date.from(currentDateTime.minusDays(1).toInstant())
            )

            // When
            restartFreeReadDateUseCase(expiredFreeReadUser)

            // Then
            coVerify {
                mockUserRepository.updateUser(
                    expiredFreeReadUser.copy(
                        startFreeReadDate = Date.from(currentDateTime.plusDays(1).toInstant()),
                        hasFreeRead = true,
                        articlesShownCount = 0,
                        visitedArticleUrls = emptyList()
                    )
                )
            }
        }

    @Test
    fun `Given a user with a valid free read date, When invoke is called, Then do not update user`() =
        runTest {
            // Given
            val currentDateTime = ZonedDateTime.now(ZoneId.systemDefault())
            coEvery { mockCurrentTime.getCurrentTime() } returns currentDateTime
            val validFreeReadUser = User(
                premium = false,
                expirationPremiumDate = null,
                subscriptionMonths = 0,
                startPremiumDate = null,
                visitedArticleUrls = listOf("https://example.com/article1"),
                articlesShownCount = 3,
                hasFreeRead = true,
                startFreeReadDate = Date.from(currentDateTime.plusDays(1).toInstant())
            )

            // When
            restartFreeReadDateUseCase(validFreeReadUser)

            // Then
            coVerify(exactly = 0) { mockUserRepository.updateUser(any()) }
        }
}