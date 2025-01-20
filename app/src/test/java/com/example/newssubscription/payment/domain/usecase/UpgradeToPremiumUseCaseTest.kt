package com.example.newssubscription.payment.domain.usecase

import com.example.news.core.domain.model.User
import com.example.newssubscription.core.domain.repository.UserRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.ZonedDateTime
import java.util.Date

class UpgradeToPremiumUseCaseTest {

    // Mock the UserRepository dependency
    private val userRepository: UserRepository = mockk(relaxed = true)

    // Initialize the use case with the mocked repository
    private val upgradeToPremiumUseCase = UpgradeToPremiumUseCase(userRepository)

    @Test
    fun `given valid user and subscription details, when invoked, then updates user repository`() =
        runTest {
            // Given: A user object, start premium date, and subscription months
            val user = User(
                id = "123",
                premium = false,
                expirationPremiumDate = null,
                startPremiumDate = null,
                subscriptionMonths = 0,
                hasFreeRead = true,
                startFreeReadDate = Date.from(Date().toInstant()),
                articlesShownCount = 5,
                visitedArticleUrls = listOf("example.com")
            )
            val startPremiumDate = ZonedDateTime.now()
            val subscriptionMonths = 6
            val expectedExpirationDate = startPremiumDate.plusMonths(subscriptionMonths.toLong())

            // When: The use case is invoked
            upgradeToPremiumUseCase(user, startPremiumDate, subscriptionMonths)

            // Then: Verify the userRepository updates the user with correct values
            coVerify {
                userRepository.updateUser(
                    user.copy(
                        expirationPremiumDate = Date.from(expectedExpirationDate.toInstant()),
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
}