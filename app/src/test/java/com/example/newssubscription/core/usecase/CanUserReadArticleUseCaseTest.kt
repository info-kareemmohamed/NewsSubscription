package com.example.newssubscription.core.usecase

import com.example.news.core.domain.model.User
import com.example.newssubscription.core.domain.repository.UserRepository
import com.example.newssubscription.core.domain.usecase.CanUserReadArticleUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CanUserReadArticleUseCaseTest {

    private val mockUserRepository: UserRepository = mockk(relaxed = true)
    private val canUserReadArticleUseCase = CanUserReadArticleUseCase(mockUserRepository)

    @Test
    fun `Given a premium user, When invoking use case, Then user can read any article`() = runTest {
        // Given
        val premiumUser = User(
            premium = true,
            hasFreeRead = false,
            articlesShownCount = 0,
            visitedArticleUrls = emptyList()
        )
        coEvery { mockUserRepository.getUser() } returns premiumUser

        // When
        val result = canUserReadArticleUseCase("https://example.com/article", 5)

        // Then
        assertEquals(true, result)
        coVerify(exactly = 0) { mockUserRepository.updateUser(any()) }
    }

    @Test
    fun `Given a non-premium user with free reads, When reading a new article, Then allow and update user`() =
        runTest {
            // Given
            val nonPremiumUser = User(
                premium = false,
                hasFreeRead = true,
                articlesShownCount = 2,
                visitedArticleUrls = listOf("https://example.com/article1")
            )
            coEvery { mockUserRepository.getUser() } returns nonPremiumUser

            // When
            val result = canUserReadArticleUseCase("https://example.com/article2", 5)

            // Then
            assertEquals(true, result)
            coVerify {
                mockUserRepository.updateUser(
                    User(
                        premium = false,
                        hasFreeRead = true,
                        articlesShownCount = 3,
                        visitedArticleUrls = listOf(
                            "https://example.com/article1",
                            "https://example.com/article2"
                        )
                    )
                )
            }
        }

    @Test
    fun `Given a non-premium user with no free reads, When reading a new article, Then deny access`() =
        runTest {
            // Given
            val nonPremiumUser = User(
                premium = false,
                hasFreeRead = false,
                articlesShownCount = 5,
                visitedArticleUrls = listOf("https://example.com/article1")
            )
            coEvery { mockUserRepository.getUser() } returns nonPremiumUser

            // When
            val result = canUserReadArticleUseCase("https://example.com/article2", 5)

            // Then
            assertEquals(false, result)
            coVerify(exactly = 0) { mockUserRepository.updateUser(any()) }
        }

    @Test
    fun `Given a non-premium user, When re-reading an already visited article, Then allow access`() =
        runTest {
            // Given
            val nonPremiumUser = User(
                premium = false,
                hasFreeRead = false,
                articlesShownCount = 5,
                visitedArticleUrls = listOf("https://example.com/article1")
            )
            coEvery { mockUserRepository.getUser() } returns nonPremiumUser

            // When
            val result = canUserReadArticleUseCase("https://example.com/article1", 5)

            // Then
            assertEquals(true, result)
            coVerify(exactly = 0) { mockUserRepository.updateUser(any()) }
        }

    @Test
    fun `Given no user found, When invoking use case, Then deny access`() = runTest {
        // Given
        coEvery { mockUserRepository.getUser() } returns null

        // When
        val result = canUserReadArticleUseCase("https://example.com/article", 5)

        // Then
        assertEquals(false, result)
        coVerify(exactly = 0) { mockUserRepository.updateUser(any()) }
    }
}
