package com.example.newssubscription.onboarding.presentation.mvvm


import com.example.newssubscription.TestCoroutineRule
import com.example.newssubscription.core.domain.repository.LocalUserAppEntry
import com.example.newssubscription.onboarding.data.PageData
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnBoardingViewModelTest {

    // Mock dependencies
    private val pageData = PageData
    private val localUserAppEntry: LocalUserAppEntry = mockk()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: OnBoardingViewModel

    @Before
    fun setup() {
        viewModel = OnBoardingViewModel(pageData, localUserAppEntry)
    }

    @Test
    fun `when ViewModel is initialized, then pages should be loaded correctly`() {
        // When
        val actualPages = viewModel.pages

        // Then
        assertEquals(pageData.pages, actualPages)
        assertEquals(0, viewModel.currentPage)
        assertFalse(viewModel.isLastPage)
    }

    @Test
    fun `when changePage is called, then currentPage should update correctly`() {
        // Given
        val newPage = 1

        // When
        viewModel.changePage(newPage)

        // Then
        assertEquals(newPage, viewModel.currentPage)
        assertFalse(viewModel.isLastPage)
    }

    @Test
    fun `when changePage is called with a negative index, then currentPage should stay at the first page`() {
        // Given
        val invalidPage = -1

        // When
        viewModel.changePage(invalidPage)

        // Then
        assertEquals(0, viewModel.currentPage) // Ensures currentPage stays at 0
        assertFalse(viewModel.isLastPage) // Verifies it's not the last page
    }

    @Test
    fun `when changePage is called with an index greater than the last page, then currentPage should stay at the last page`() {
        // Given
        val invalidPage = 100
        val lastPageIndex = viewModel.pages.size - 1

        // When
        viewModel.changePage(invalidPage)

        // Then
        assertEquals(
            lastPageIndex,
            viewModel.currentPage
        ) // Ensures currentPage stays at the last page

        assertTrue(viewModel.isLastPage) // Verifies it's the last page
    }

    @Test
    fun `when changePage is called with the last page, then isLastPage should be true`() {
        // Given
        val lastPageIndex = 2

        // When
        viewModel.changePage(lastPageIndex)

        // Then
        assertEquals(lastPageIndex, viewModel.currentPage)
        assertTrue(viewModel.isLastPage)
    }

}
