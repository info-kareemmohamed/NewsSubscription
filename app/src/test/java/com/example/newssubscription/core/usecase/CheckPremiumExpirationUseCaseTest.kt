package com.example.newssubscription.core.usecase

import com.example.news.core.domain.model.User
import com.example.newssubscription.core.domain.repository.UserRepository
import com.example.newssubscription.core.domain.usecase.CheckPremiumExpirationUseCase
import com.example.newssubscription.payment.domain.repository.CurrentTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

class CheckPremiumExpirationUseCaseTest {

    private val mockCurrentTime: CurrentTime = mockk()
    private val mockUserRepository: UserRepository = mockk(relaxed = true)
    private val checkPremiumExpirationUseCase =
        CheckPremiumExpirationUseCase(mockCurrentTime, mockUserRepository)

    @Test
    fun `Given a premium user with valid subscription, When invoke is called, Then do not update user`() =
        runTest {
            // Given
            val currentDateTime = ZonedDateTime.now(ZoneId.systemDefault())
            coEvery { mockCurrentTime.getCurrentTime() } returns currentDateTime
            val validPremiumUser = User(
                premium = true,
                expirationPremiumDate = Date.from(
                    currentDateTime.plusDays(10).toInstant()
                ),
                subscriptionMonths = 6,
                startPremiumDate = Date.from(
                    currentDateTime.minusMonths(5).toInstant()
                ),
                visitedArticleUrls = listOf("https://example.com/article1"),
                articlesShownCount = 3,
                hasFreeRead = false,
                startFreeReadDate = null
            )

            // When
            checkPremiumExpirationUseCase(validPremiumUser)

            // Then
            coVerify(exactly = 0) { mockUserRepository.updateUser(any()) }
        }

    @Test
    fun `Given a premium user with expired subscription, When invoke is called, Then update user to non-premium`() =
        runTest {
            // Given
            val currentDateTime = ZonedDateTime.now(ZoneId.systemDefault())
            coEvery { mockCurrentTime.getCurrentTime() } returns currentDateTime
            val expiredPremiumUser = User(
                premium = true,
                expirationPremiumDate = Date.from(
                    currentDateTime.minusDays(1).toInstant()
                ),
                subscriptionMonths = 6,
                startPremiumDate = Date.from(
                    currentDateTime.minusMonths(6).toInstant()
                ),
                visitedArticleUrls = listOf("https://example.com/article1"),
                articlesShownCount = 3,
                hasFreeRead = false,
                startFreeReadDate = null
            )

            // When
            checkPremiumExpirationUseCase(expiredPremiumUser)

            // Then
            coVerify {
                mockUserRepository.updateUser(
                    expiredPremiumUser.copy(
                        premium = false,
                        subscriptionMonths = 0,
                        startPremiumDate = null,
                        expirationPremiumDate = null,
                        visitedArticleUrls = emptyList(),
                        articlesShownCount = 0,
                        hasFreeRead = true,
                        startFreeReadDate = Date.from(
                            currentDateTime.plusDays(1).toInstant()
                        )
                    )
                )
            }
        }

    @Test
    fun `Given a non-premium user, When invoke is called, Then do not update user`() = runTest {
        // Given
        val currentDateTime = ZonedDateTime.now(ZoneId.systemDefault())
        coEvery { mockCurrentTime.getCurrentTime() } returns currentDateTime
        val nonPremiumUser = User(
            premium = false,
            expirationPremiumDate = null,
            subscriptionMonths = 0,
            startPremiumDate = null,
            visitedArticleUrls = listOf("https://example.com/article1"),
            articlesShownCount = 3,
            hasFreeRead = true,
            startFreeReadDate = Date.from(
                currentDateTime.plusDays(1).toInstant()
            )
        )

        // When
        checkPremiumExpirationUseCase(nonPremiumUser)

        // Then
        coVerify(exactly = 0) { mockUserRepository.updateUser(any()) }
    }
}