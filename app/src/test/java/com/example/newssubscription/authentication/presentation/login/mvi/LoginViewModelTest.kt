package com.example.newssubscription.authentication.presentation.login.mvi

import androidx.credentials.GetCredentialResponse
import app.cash.turbine.test
import com.example.news.core.domain.model.User
import com.example.newssubscription.TestCoroutineRule
import com.example.newssubscription.authentication.domain.repository.AuthRepository
import com.example.newssubscription.authentication.domain.usecase.ValidateEmailUseCase
import com.example.newssubscription.authentication.domain.usecase.ValidatePasswordUseCase
import com.example.newssubscription.authentication.domain.util.AuthError
import com.example.newssubscription.core.domain.util.Result
import com.example.newssubscription.authentication.domain.util.ValidationError.EmailError
import com.example.newssubscription.authentication.domain.util.ValidationError.PasswordError
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: LoginViewModel
    private val authRepository: AuthRepository = mockk()
    private val validateEmailUseCase: ValidateEmailUseCase = mockk()
    private val validatePasswordUseCase: ValidatePasswordUseCase = mockk()

    @Before
    fun setUp() {
        viewModel = LoginViewModel(authRepository, validateEmailUseCase, validatePasswordUseCase)
    }

    @Test
    fun `should update email error state correctly on multiple email changes`() = runTest {
        val invalidEmail = "test@example."
        val validEmail = "valid@example.com"
        val emailError = EmailError.EMAIL_INVALID_FORMAT

        // Given: An invalid email is provided
        every { validateEmailUseCase(invalidEmail) } returns Result.Error(emailError)

        // When: The user changes the email to an invalid one
        viewModel.onIntent(LoginIntent.EmailChanged(invalidEmail))

        // Then: The state should reflect the invalid email and show the corresponding error
        viewModel.loginState.test {
            val state = awaitItem()
            assertEquals(invalidEmail, state.email)
            assertEquals(emailError, state.emailErrorMessage)
        }

        // Given: A valid email is provided
        every { validateEmailUseCase(validEmail) } returns Result.Success(Unit)

        // When: The user changes the email to a valid one
        viewModel.onIntent(LoginIntent.EmailChanged(validEmail))

        // Then: The state should reflect the valid email and clear the error message
        viewModel.loginState.test {
            val state = awaitItem()
            assertEquals(validEmail, state.email)
            assertNull(state.emailErrorMessage)
        }

        // Given: An invalid email is provided again
        every { validateEmailUseCase(invalidEmail) } returns Result.Error(emailError)

        // When: The user changes the email back to an invalid one
        viewModel.onIntent(LoginIntent.EmailChanged(invalidEmail))

        // Then: The state should reflect the invalid email and show the corresponding error again
        viewModel.loginState.test {
            val state = awaitItem()
            assertEquals(invalidEmail, state.email)
            assertEquals(emailError, state.emailErrorMessage)
        }
    }


    @Test
    fun `should update password error state correctly on multiple password changes`() = runTest {
        val shortPassword = "123"
        val validPassword = "ValidPass123"
        val passwordError = PasswordError.PASSWORD_TOO_SHORT

        // Given: A short password that does not meet the requirements
        every { validatePasswordUseCase(shortPassword) } returns Result.Error(passwordError)

        // When: The user changes the password to a short one
        viewModel.onIntent(LoginIntent.PasswordChanged(shortPassword))

        // Then: The state should reflect the short password and the corresponding error
        viewModel.loginState.test {
            val state = awaitItem()
            assertEquals(shortPassword, state.password)
            assertEquals(passwordError, state.passwordErrorMessage)
        }

        // Given: A valid password
        every { validatePasswordUseCase(validPassword) } returns Result.Success(Unit)

        // When: The user changes the password to a valid one
        viewModel.onIntent(LoginIntent.PasswordChanged(validPassword))

        // Then: The state should reflect the valid password and no error message
        viewModel.loginState.test {
            val state = awaitItem()
            assertEquals(validPassword, state.password)
            assertNull(state.passwordErrorMessage)
        }

        // Given: A short password again
        every { validatePasswordUseCase(shortPassword) } returns Result.Error(passwordError)

        // When: The user changes the password back to a short one
        viewModel.onIntent(LoginIntent.PasswordChanged(shortPassword))

        // Then: The state should reflect the short password and the corresponding error
        viewModel.loginState.test {
            val state = awaitItem()
            assertEquals(shortPassword, state.password)
            assertEquals(passwordError, state.passwordErrorMessage)
        }
    }


    @Test
    fun `should emit errors when login clicked without email and password`() = runTest {
        // Given: No credentials provided
        val emailError = EmailError.EMAIL_EMPTY
        val passwordError = PasswordError.PASSWORD_EMPTY
        every { validateEmailUseCase(any()) } returns Result.Error(emailError)
        every { validatePasswordUseCase(any()) } returns Result.Error(passwordError)

        // When: User clicks login without entering any input
        viewModel.onIntent(LoginIntent.LoginClicked)

        // Then: State should emit errors for empty email and password, and not proceed with login
        viewModel.loginState.test {
            val state = awaitItem()
            assertEquals(emailError, state.emailErrorMessage) // Email error emitted
            assertEquals(passwordError, state.passwordErrorMessage) // Password error emitted
            assertFalse(state.isLoading) // Loading is not triggered
            assertFalse(state.isLoadingGoogle) // Loading is not triggered
            assertNull(state.errorMessage) // No general error message
        }
    }


    @Test
    fun `should emit success and update state when login succeeds`() = runTest {
        // Given: Valid email and password, and successful login response
        val email = "valid@example.com"
        val password = "password123"
        every { validateEmailUseCase(any()) } returns Result.Success(Unit)
        every { validatePasswordUseCase(any()) } returns Result.Success(Unit)
        coEvery { authRepository.signIn(any(), any()) } returns Result.Success(User())

        // When: User enters valid credentials and clicks login
        viewModel.onIntent(LoginIntent.EmailChanged(email))
        viewModel.onIntent(LoginIntent.PasswordChanged(password))
        viewModel.onIntent(LoginIntent.LoginClicked)

        // Then: Collect all emitted states to verify the flow of updates
        viewModel.loginState.test {
            awaitItem()// Skip initial state

            // Verify loading state is emitted first
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading) // isLoading should be true
            assertFalse(loadingState.isLoadingGoogle) // isLoadingGoogle should be false
            assertNull(loadingState.errorMessage) // No error message yet

            // Verify final state after login success
            val finalState = awaitItem()
            assertEquals(email, finalState.email) // Email remains the same
            assertEquals(password, finalState.password) // Password remains the same
            assertFalse(finalState.isLoading) // isLoading should be false
            assertFalse(finalState.isLoadingGoogle) // isLoadingGoogle should be false
            assertNull(finalState.errorMessage) // No error message in final state
        }

        // Verify loginSuccessfully event
        viewModel.loginSuccessfully.test {
            assertTrue(awaitItem()) // Login success emitted
        }
    }


    @Test
    fun `should handle login failure and update state with error`() = runTest {
        // Given: Valid email and password, but login fails
        val email = "valid@example.com"
        val password = "password123"
        val authError = AuthError.INVALID_CREDENTIALS
        every { validateEmailUseCase(any()) } returns Result.Success(Unit)
        every { validatePasswordUseCase(any()) } returns Result.Success(Unit)
        coEvery { authRepository.signIn(any(), any()) } returns Result.Error(authError)

        // When: User enters credentials and attempts login
        viewModel.onIntent(LoginIntent.EmailChanged(email))
        viewModel.onIntent(LoginIntent.PasswordChanged(password))
        viewModel.onIntent(LoginIntent.LoginClicked)

        // Then: Verify state changes for loading and error
        viewModel.loginState.test {
            awaitItem() // Skip initial state

            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertFalse(loadingState.isLoadingGoogle)
            assertNull(loadingState.errorMessage)

            // Error state
            val errorState = awaitItem()
            assertEquals(email, errorState.email)
            assertEquals(password, errorState.password)
            assertEquals(authError, errorState.errorMessage)
            assertFalse(errorState.isLoading)
            assertFalse(errorState.isLoadingGoogle)
        }
    }


    @Test
    fun `should emit success and update state on Google sign-in success`() = runTest {
        // Given: Valid Google credential and successful sign-in
        val credential = mockk<GetCredentialResponse>()
        coEvery { authRepository.googleSignIn(credential) } returns Result.Success(User())

        // When: User clicks Google Sign-In
        viewModel.onIntent(LoginIntent.GoogleSignInClicked(credential))

        // Then: Verify state updates and success emission
        viewModel.loginState.test {
            awaitItem() // Skip initial state

            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoadingGoogle)
            assertFalse(loadingState.isLoading)
            assertNull(loadingState.errorMessage)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoadingGoogle)
            assertFalse(successState.isLoading)
            assertNull(successState.errorMessage)
        }

        viewModel.loginSuccessfully.test {
            assertTrue(awaitItem()) // Success emitted
        }
    }


    @Test
    fun `should update state with error on Google sign-in failure`() = runTest {
        // Given: Valid Google credential and failed sign-in
        val credential = mockk<GetCredentialResponse>()
        val authError = AuthError.UNKNOWN_ERROR
        coEvery { authRepository.googleSignIn(credential) } returns Result.Error(authError)

        // When: User clicks Google Sign-In
        viewModel.onIntent(LoginIntent.GoogleSignInClicked(credential))

        // Then: Verify state reflects error and loading stops
        viewModel.loginState.test {
            awaitItem() // Skip initial state

            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoadingGoogle)
            assertFalse(loadingState.isLoading)
            assertNull(loadingState.errorMessage)

            // Error state
            val errorState = awaitItem()
            assertEquals(authError, errorState.errorMessage)
            assertFalse(errorState.isLoadingGoogle)
            assertFalse(errorState.isLoading)
        }
    }


    @Test
    fun `should update state correctly on forgot password success`() = runTest {
        // Given: Valid email and successful forgot password response
        val email = "valid@example.com"
        every { validateEmailUseCase(email) } returns Result.Success(Unit)
        coEvery { authRepository.forgotPassword(email) } returns Result.Success(Unit)

        // When: User enters valid email and triggers forgot password action
        viewModel.onIntent(LoginIntent.EmailChanged(email))
        viewModel.onIntent(LoginIntent.ForgotPasswordClicked)

        // Then: Verify state transitions for loading and success
        viewModel.loginState.test {
            awaitItem() // Skip initial state

            // Loading state
            val loadingState = awaitItem()
            assertEquals(email, loadingState.email)
            assertTrue(loadingState.isLoading)
            assertFalse(loadingState.isLoadingGoogle)
            assertNull(loadingState.errorMessage)
            assertFalse(loadingState.forgotPasswordSuccessMessage)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertFalse(successState.isLoadingGoogle)
            assertTrue(successState.forgotPasswordSuccessMessage)
            assertNull(successState.errorMessage)
        }
    }


    @Test
    fun `should update state correctly on forgot password failure`() = runTest {
        // Given: Valid email and failed forgot password response with error
        val email = "valid@example.com"
        val authError = AuthError.UNKNOWN_ERROR
        every { validateEmailUseCase(email) } returns Result.Success(Unit)
        coEvery { authRepository.forgotPassword(email) } returns Result.Error(authError)

        // When: User enters valid email and triggers forgot password action
        viewModel.onIntent(LoginIntent.EmailChanged(email))
        viewModel.onIntent(LoginIntent.ForgotPasswordClicked)

        // Then: Verify state transitions for loading and error
        viewModel.loginState.test {
            awaitItem() // Skip initial state

            // Loading state
            val loadingState = awaitItem()
            assertEquals(email, loadingState.email)
            assertTrue(loadingState.isLoading)
            assertFalse(loadingState.isLoadingGoogle)
            assertNull(loadingState.errorMessage)
            assertFalse(loadingState.forgotPasswordSuccessMessage)

            // Error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertFalse(errorState.isLoadingGoogle)
            assertEquals(authError, errorState.errorMessage)
            assertFalse(errorState.forgotPasswordSuccessMessage)
        }
    }


    @Test
    fun `should not update state when forgot password is called with invalid email`() = runTest {
        // Given: Invalid email format
        val email = "valid@example."
        val emailError = EmailError.EMAIL_INVALID_FORMAT
        every { validateEmailUseCase(email) } returns Result.Error(emailError)

        // When: User enters invalid email and triggers forgot password action
        viewModel.onIntent(LoginIntent.EmailChanged(email))
        viewModel.onIntent(LoginIntent.ForgotPasswordClicked)

        // Then: State should remain unchanged with the email error
        viewModel.loginState.test {
            val state = awaitItem() // Only one state should be emitted
            assertEquals(email, state.email)
            assertFalse(state.isLoading)
            assertFalse(state.isLoadingGoogle)
            assertFalse(state.forgotPasswordSuccessMessage)
            assertEquals(emailError, state.emailErrorMessage)
        }
    }


    @Test
    fun `should emit email error when forgot password is clicked without email`() = runTest {
        // Given: No email provided
        val emailError = EmailError.EMAIL_EMPTY
        every { validateEmailUseCase(any()) } returns Result.Error(emailError)

        // When: Forgot password clicked without email
        viewModel.onIntent(LoginIntent.ForgotPasswordClicked)

        // Then: Email error is emitted, and loading or general error doesn't occur
        viewModel.loginState.test {
            val state = awaitItem()
            assertEquals(emailError, state.emailErrorMessage) // Email error due to empty input
            assertFalse(state.isLoading) // No loading state triggered
            assertFalse(state.isLoadingGoogle) // No loading state triggered
            assertNull(state.errorMessage) // No general error message
        }
    }

}