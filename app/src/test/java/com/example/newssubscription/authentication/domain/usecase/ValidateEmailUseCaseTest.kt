package com.example.newssubscription.authentication.domain.usecase

import com.example.newssubscription.core.domain.util.Result
import com.example.newssubscription.authentication.domain.util.ValidationError.EmailError
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateEmailUseCaseTest {

    private lateinit var validateEmailUseCase: ValidateEmailUseCase

    @Before
    fun setUp() {
        validateEmailUseCase = ValidateEmailUseCase()
    }

    @Test
    fun `should return EMAIL_EMPTY error when email is blank`() {
        // Given: A blank email
        val email = ""

        // When: The validateEmailUseCase is invoked
        val result = validateEmailUseCase(email)

        // Then: The result should be EMAIL_EMPTY error
        assertEquals(Result.Error(EmailError.EMAIL_EMPTY), result)
    }

    @Test
    fun `should return EMAIL_INVALID_FORMAT error when email format is invalid`() {
        // Given: An email with an invalid format
        val email = "invalid-email"

        // When: The validateEmailUseCase is invoked
        val result = validateEmailUseCase(email)

        // Then: The result should be EMAIL_INVALID_FORMAT error
        assertEquals(Result.Error(EmailError.EMAIL_INVALID_FORMAT), result)
    }

    @Test
    fun `should return Success when email format is valid`() {
        // Given: A valid email
        val email = "test@example.com"

        // When: The validateEmailUseCase is invoked
        val result = validateEmailUseCase(email)

        // Then: The result should be Success
        assertEquals(Result.Success(Unit), result)
    }
}
