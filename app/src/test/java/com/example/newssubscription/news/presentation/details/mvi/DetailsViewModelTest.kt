package com.example.newssubscription.news.presentation.details.mvi

import app.cash.turbine.test
import com.example.newssubscription.TestCoroutineRule
import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.domain.usecase.ToggleOrInsertBookmarkUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val toggleOrInsertBookmarkUseCase: ToggleOrInsertBookmarkUseCase = mockk()
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        // Initialize the ViewModel with mocked dependencies
        viewModel = DetailsViewModel(
            toggleOrInsertBookmarkUseCase = toggleOrInsertBookmarkUseCase
        )
    }

    @Test
    fun `should update state with article details when LoadArticle intent is triggered`() =
        runTest {
            // Given: A mocked article retrieved from the repository
            val article = getArticle()

            // When: The LoadArticle intent is triggered
            viewModel.onIntent(DetailsIntent.LoadArticle(article))

            // Then: The state is updated with the article details
            viewModel.state.test {
                awaitItem() // Skip initial state
                val updatedState = awaitItem()
                assertEquals(article, updatedState.article)
                assertEquals(true, updatedState.isBookmarked)
            }
        }

    @Test
    fun `should toggle isBookmarked when OnBookmarkClick intent is triggered`() = runTest {
        // Given: A mocked article retrieved from the repository
        val article = getArticle()

        // When: The LoadArticle intent is triggered to load the article
        viewModel.onIntent(DetailsIntent.LoadArticle(article))

        // Then: The state is updated with the article's bookmark status
        viewModel.state.test {
            awaitItem() // Skip initial state
            val updatedState = awaitItem()
            assertEquals(article.isBookMarked, updatedState.isBookmarked)
        }

        // When: The OnBookmarkClick intent is triggered to toggle the bookmark status
        viewModel.onIntent(DetailsIntent.OnBookmarkClick)

        // Then: The isBookmarked state is toggled
        viewModel.state.test {
            val updatedState = awaitItem()
            assertEquals(!article.isBookMarked, updatedState.isBookmarked)
        }
    }

    @Test
    fun `should toggle bookmark status in repository when SaveFinalBookmarkInData intent is triggered`() =
        runTest {
            // Given: A mocked article and repository behavior
            val article = getArticle()
            coEvery { toggleOrInsertBookmarkUseCase(article) } just Runs

            // When: The LoadArticle intent is triggered to load the article
            viewModel.onIntent(DetailsIntent.LoadArticle(article))

            // Then: The state is updated with the article's bookmark status
            viewModel.state.test {
                awaitItem() // Skip initial state
                val updatedState = awaitItem()
                assertEquals(article.isBookMarked, updatedState.isBookmarked)
            }

            // When: The OnBookmarkClick intent is triggered to toggle the bookmark status
            viewModel.onIntent(DetailsIntent.OnBookmarkClick)

            // Then: The isBookmarked state is toggled
            viewModel.state.test {
                val updatedState = awaitItem()
                assertEquals(!article.isBookMarked, updatedState.isBookmarked)
            }

            // When: The SaveFinalBookmarkInData intent is triggered to save the final bookmark status
            viewModel.onIntent(DetailsIntent.SaveFinalBookmarkInData)

            // Wait for the coroutine to complete
            advanceUntilIdle()

            // Then: The repository toggles the bookmark status
            coVerify { toggleOrInsertBookmarkUseCase(article) }
        }

    private fun getArticle(): Article =
        Article(
            url = "test-url",
            title = "Test Article",
            content = "This is a test article.",
            isBookMarked = true,
            imageUrl = "https://example.com/image.jpg",
            publishedAt = "2023-09-01T12:00:00Z",
            sourceName = "Test Source",
            author = "Test Author",
            description = "Test Description"
        )
}