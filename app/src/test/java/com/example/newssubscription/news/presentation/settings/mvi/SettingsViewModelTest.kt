package com.example.newssubscription.news.presentation.settings.mvi

import com.example.news.core.domain.model.User
import com.example.newssubscription.TestCoroutineRule
import com.example.newssubscription.core.domain.repository.UserRepository
import com.example.newssubscription.news.domain.repository.PhotoRepository
import com.example.newssubscription.news.domain.usecase.SignOutUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val signOutUseCase: SignOutUseCase = mockk()
    private val photoRepository: PhotoRepository = mockk()
    private val userRepository: UserRepository = mockk()


    @Test
    fun `Given user data, When ViewModel is created, Then state should be updated with user data`() =
        runTest {
            // Given
            val user = getUser()
            coEvery { userRepository.getAuthenticatedUserAsFlow() } returns flowOf(user)

            // When
            val viewModel = SettingsViewModel(signOutUseCase, photoRepository, userRepository)


            // Wait for the coroutine to complete
            advanceUntilIdle()

            // Then
            assertEquals(user, viewModel.state.value.user)

        }


    @Test
    fun `Given OnImageChange intent with valid byteArray, When onIntent is called, Then state should be updated with new photo URL`() =
        runTest {
            // Given
            val byteArray = ByteArray(10)
            val newPhotoUrl = "https://example.com/new-photo.jpg"
            val user = getUser()
            coEvery { userRepository.getAuthenticatedUserAsFlow() } returns flowOf(user)
            coEvery {
                photoRepository.uploadPhoto(user.email, user.profilePictureUrl, byteArray)
            } returns newPhotoUrl
            coEvery { userRepository.updateUser(any()) } returns Unit

            // When
            val viewModel = SettingsViewModel(signOutUseCase, photoRepository, userRepository)
            viewModel.onIntent(SettingsIntent.OnImageChange(byteArray))

            // Wait for the coroutine to complete
            advanceUntilIdle()

            // Then
            assertEquals(newPhotoUrl, viewModel.state.value.user?.profilePictureUrl)
            coVerify { photoRepository.uploadPhoto(user.email, user.profilePictureUrl, byteArray) }
            coVerify { userRepository.updateUser(any()) }
        }

    @Test
    fun `Given OnImageChange intent with null byteArray, When onIntent is called, Then state should not be updated`() =
        runTest {
            // Given
            val intent = SettingsIntent.OnImageChange(null)
            coEvery { userRepository.getAuthenticatedUserAsFlow() } returns flowOf(getUser())

            // When
            val viewModel = SettingsViewModel(signOutUseCase, photoRepository, userRepository)
            viewModel.onIntent(intent)

            // Wait for the coroutine to complete
            advanceUntilIdle()

            // Then
            assertEquals(null, viewModel.state.value.user?.profilePictureUrl)
            coVerify(exactly = 0) { photoRepository.uploadPhoto(any(), any(), any()) }
        }

    @Test
    fun `Given OnImageChange intent with exception, When onIntent is called, Then state should have error`() =
        runTest {
            // Given
            val byteArray = ByteArray(10)
            val exception = Exception("Upload failed")
            coEvery { userRepository.getAuthenticatedUserAsFlow() } returns flowOf(getUser())
            coEvery { photoRepository.uploadPhoto(any(), any(), any()) } throws exception

            // When
            val viewModel = SettingsViewModel(signOutUseCase, photoRepository, userRepository)
            viewModel.onIntent(SettingsIntent.OnImageChange(byteArray))

            // Wait for the coroutine to complete
            advanceUntilIdle()

            // Then
            assertEquals("Upload failed", viewModel.state.value.error)
            assertEquals(false, viewModel.state.value.imageLoading)
        }

    private fun getUser() = User(id = "1", name = "Kareem Mohamed")
}