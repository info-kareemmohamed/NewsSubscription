package com.example.newssubscription.authentication.presentation.signup.mvi

import androidx.credentials.GetCredentialResponse
import app.cash.turbine.test
import com.example.news.core.domain.model.User
import com.example.newssubscription.TestCoroutineRule
import com.example.newssubscription.authentication.domain.repository.AuthRepository
import com.example.newssubscription.authentication.domain.usecase.ValidateConfirmPasswordUseCase
import com.example.newssubscription.authentication.domain.usecase.ValidateEmailUseCase
import com.example.newssubscription.authentication.domain.usecase.ValidatePasswordUseCase
import com.example.newssubscription.authentication.domain.usecase.ValidateUserNameUseCase
import com.example.newssubscription.authentication.domain.util.AuthError
import com.example.newssubscription.core.domain.util.Result
import com.example.newssubscription.authentication.domain.util.ValidationError.EmailError
import com.example.newssubscription.authentication.domain.util.ValidationError.PasswordError
import com.example.newssubscription.authentication.domain.util.ValidationError.UsernameError
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: SignUpViewModel
    private val authRepository: AuthRepository = mockk()
    private val validateEmailUseCase: ValidateEmailUseCase = mockk()
    private val validatePasswordUseCase: ValidatePasswordUseCase = mockk()
    private val validateUserNameUseCase: ValidateUserNameUseCase = mockk()
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase = mockk()

    @Before
    fun setUp() {
        viewModel = SignUpViewModel(
            authRepository,
            validateEmailUseCase,
            validatePasswordUseCase,
            validateUserNameUseCase,
            validateConfirmPasswordUseCase
        )
    }

    @Test
    fun `should update state with validation errors and successful input changes`() = runTest {
        // Given: Invalid and valid user inputs
        val invalidUsername = "123"
        val validUsername = "TestUser1"
        val invalidEmail = "test@example."
        val validEmail = "test1@example.com"
        val invalidPassword = "123"
        val validPassword = "123456"

        // Mocking validation errors for invalid input
        every { validateEmailUseCase(any()) } returns Result.Error(EmailError.EMAIL_INVALID_FORMAT)
        every { validatePasswordUseCase(any()) } returns Result.Error(PasswordError.PASSWORD_TOO_SHORT)
        every { validateUserNameUseCase(any()) } returns Result.Error(UsernameError.USERNAME_EMPTY)
        every {
            validateConfirmPasswordUseCase(
                any(),
                any()
            )
        } returns Result.Error(PasswordError.CONFIRM_PASSWORD_DOES_NOT_MATCH)

        // When: User enters invalid credentials
        viewModel.onIntent(SignUpIntent.UsernameChanged(invalidUsername))
        viewModel.onIntent(SignUpIntent.EmailChanged(invalidEmail))
        viewModel.onIntent(SignUpIntent.PasswordChanged(validPassword))
        viewModel.onIntent(SignUpIntent.ConfirmPasswordChanged(invalidPassword))

        // Then: State should reflect the validation errors
        viewModel.signUpState.test {
            val state = awaitItem()
            assertEquals(invalidUsername, state.username)
            assertEquals(invalidEmail, state.email)
            assertEquals(validPassword, state.password)
            assertEquals(invalidPassword, state.confirmPassword)
            assertEquals(EmailError.EMAIL_INVALID_FORMAT, state.emailErrorMessage)
            assertEquals(PasswordError.PASSWORD_TOO_SHORT, state.passwordErrorMessage)
            assertEquals(UsernameError.USERNAME_EMPTY, state.usernameErrorMessage)
            assertEquals(
                PasswordError.CONFIRM_PASSWORD_DOES_NOT_MATCH,
                state.confirmPasswordErrorMessage
            )
        }

        // Mocking successful validation for correct input
        every { validateEmailUseCase(any()) } returns Result.Success(Unit)
        every { validatePasswordUseCase(any()) } returns Result.Success(Unit)
        every { validateUserNameUseCase(any()) } returns Result.Success(Unit)
        every { validateConfirmPasswordUseCase(any(), any()) } returns Result.Success(Unit)

        // When: User enters valid credentials
        viewModel.onIntent(SignUpIntent.UsernameChanged(validUsername))
        viewModel.onIntent(SignUpIntent.EmailChanged(validEmail))
        viewModel.onIntent(SignUpIntent.PasswordChanged(validPassword))
        viewModel.onIntent(SignUpIntent.ConfirmPasswordChanged(validPassword))

        // Then: State should reflect the valid input without errors
        viewModel.signUpState.test {
            val state = awaitItem()
            assertEquals(validUsername, state.username)
            assertEquals(validEmail, state.email)
            assertEquals(validPassword, state.password)
            assertEquals(validPassword, state.confirmPassword)
            assertNull(state.emailErrorMessage)
            assertNull(state.passwordErrorMessage)
            assertNull(state.usernameErrorMessage)
            assertNull(state.confirmPasswordErrorMessage)
        }
    }


    @Test
    fun `should sign up successfully and handle loading state`() = runTest {
        // Given: Valid user input and successful validations
        val username = "TestUser"
        val email = "test@example.com"
        val password = "123456"

        every { validateEmailUseCase(any()) } returns Result.Success(Unit)
        every { validatePasswordUseCase(any()) } returns Result.Success(Unit)
        every { validateUserNameUseCase(any()) } returns Result.Success(Unit)
        every { validateConfirmPasswordUseCase(any(), any()) } returns Result.Success(Unit)
        coEvery { authRepository.signUp(any(), any(), any()) } returns Result.Success(User())

        // When: User submits the sign-up form
        viewModel.onIntent(SignUpIntent.UsernameChanged(username))
        viewModel.onIntent(SignUpIntent.EmailChanged(email))
        viewModel.onIntent(SignUpIntent.PasswordChanged(password))
        viewModel.onIntent(SignUpIntent.ConfirmPasswordChanged(password))
        viewModel.onIntent(SignUpIntent.Submit)

        // Then: Verify loading state and final signup result
        viewModel.signUpState.test {
            awaitItem() // Skip initial state

            // Verify loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading) // Loading indicator should be visible
            assertFalse(loadingState.isLoadingGoogle) // Google sign-in loading should be false
            assertNull(loadingState.errorMessage) // No error message during loading

            // Verify final state after sign-up success
            val finalState = awaitItem()
            assertFalse(finalState.isLoading) // No loading indicator after signup
            assertEquals(email, finalState.email) // Email should match the input
            assertEquals(password, finalState.password) // Password should match the input
            assertNull(finalState.errorMessage) // No error message after successful sign-up
        }

        // Verify the sign-up success flag
        viewModel.signUpSuccessfully.test {
            assertTrue(awaitItem()) // Sign-up should be successful
        }
    }


    @Test
    fun `emit errors for empty input on submit`() = runTest {
        // Given: Validation errors for empty input fields
        val emailError = EmailError.EMAIL_EMPTY
        val usernameError = UsernameError.USERNAME_EMPTY
        val passwordError = PasswordError.PASSWORD_EMPTY

        every { validateEmailUseCase(any()) } returns Result.Error(emailError)
        every { validateUserNameUseCase(any()) } returns Result.Error(usernameError)
        every { validatePasswordUseCase(any()) } returns Result.Error(passwordError)
        every { validateConfirmPasswordUseCase(any(), any()) } returns Result.Error(passwordError)

        // When: Submit intent is triggered
        viewModel.onIntent(SignUpIntent.Submit)
        advanceUntilIdle()

        // Then: The error states should be emitted correctly
        viewModel.signUpState.test {
            val finalState = awaitItem()
            assertEquals(emailError, finalState.emailErrorMessage)
            assertEquals(usernameError, finalState.usernameErrorMessage)
            assertEquals(passwordError, finalState.passwordErrorMessage)
            assertEquals(passwordError, finalState.confirmPasswordErrorMessage)
            assertFalse(finalState.isLoading)
        }
    }



    @Test
    fun `should show error when submit is clicked with invalid email`() = runTest {
        // Given: Invalid email
        val email = "invalid-email"
        every { validateEmailUseCase(any()) } returns Result.Error(EmailError.EMAIL_INVALID_FORMAT)
        every { validateUserNameUseCase(any()) } returns Result.Success(Unit)
        every { validatePasswordUseCase(any()) } returns Result.Success(Unit)
        every { validateConfirmPasswordUseCase(any(), any()) } returns Result.Success(Unit)

        // When: User clicks submit with invalid email
        viewModel.onIntent(SignUpIntent.EmailChanged(email))
        viewModel.onIntent(SignUpIntent.Submit)

        // Then: Error state should be emitted for invalid email
        advanceUntilIdle()
        viewModel.signUpState.test {
            val finalState = awaitItem()
            assertEquals(email, finalState.email)
            assertEquals(EmailError.EMAIL_INVALID_FORMAT, finalState.emailErrorMessage)
            assertFalse(finalState.isLoading)
        }
    }


    @Test
    fun `should handle Google sign-in and emit success`() = runTest {
        // Given: Successful Google sign-in mock response
        val credential: GetCredentialResponse = mockk()
        coEvery { authRepository.googleSignIn(credential) } returns Result.Success(User())

        // When: User triggers Google sign-in
        viewModel.onIntent(SignUpIntent.GoogleSignInClicked(credential))

        // Then: Verify loading and final success states
        viewModel.signUpState.test {
            awaitItem() // Skip initial state

            // Verify loading state during Google sign-in
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoadingGoogle) // Google sign-in is in progress
            assertFalse(loadingState.isLoading) // Regular loading should not be active
            assertNull(loadingState.errorMessage) // No error message during loading

            // Verify final state after Google sign-in success
            val finalState = awaitItem()
            assertFalse(finalState.isLoadingGoogle) // Google sign-in is complete
            assertFalse(finalState.isLoading) // No regular loading after completion
        }

        // Verify the sign-up success flag
        viewModel.googleSignUpSuccessfully.test {
            assertTrue(awaitItem()) // Sign-up should be successful after Google sign-in
        }
    }


    @Test
    fun `should handle Google sign-in failure and show error`() = runTest {
        // Given: Google sign-in fails with an error
        val credential: GetCredentialResponse = mockk()
        val authError = AuthError.UNKNOWN_ERROR
        coEvery { authRepository.googleSignIn(credential) } returns Result.Error(authError)

        // When: User clicks on Google sign-in
        viewModel.onIntent(SignUpIntent.GoogleSignInClicked(credential))

        // Then: Verify loading state and error emission after failure
        viewModel.signUpState.test {
            awaitItem() // Skip initial state

            // Verify loading state during Google sign-in
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoadingGoogle) // Google sign-in in progress
            assertFalse(loadingState.isLoading) // Regular loading inactive
            assertNull(loadingState.errorMessage) // No error during loading

            // Verify error state after Google sign-in failure
            val errorState = awaitItem()
            assertFalse(errorState.isLoadingGoogle) // Google sign-in complete
            assertFalse(errorState.isLoading) // No regular loading
            assertEquals(authError, errorState.errorMessage) // Ensure the error is propagated
        }
    }

}