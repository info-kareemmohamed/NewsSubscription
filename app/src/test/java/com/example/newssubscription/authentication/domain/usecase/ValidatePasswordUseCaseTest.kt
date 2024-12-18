package com.example.newssubscription.authentication.domain.usecase


import com.example.newssubscription.core.domain.util.Result
import com.example.newssubscription.authentication.domain.util.ValidationError.PasswordError
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidatePasswordUseCaseTest {

    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase

    @Before
    fun setUp() {
        validatePasswordUseCase = ValidatePasswordUseCase()
    }

    @Test
    fun `should return PASSWORD_EMPTY error when password is blank`() {
        //Given: A blank password
        val password = ""

        //When: The validatePasswordUseCase is invoked
        val result = validatePasswordUseCase(password)

        //Then: The result should be PASSWORD_EMPTY error
        assertEquals(Result.Error(PasswordError.PASSWORD_EMPTY), result)
    }

    @Test
    fun `should return PASSWORD_TOO_SHORT error when password length is less than 6`() {
        //Given: A password with length less than 6
        val password = "12345"

        //When: The validatePasswordUseCase is invoked
        val result = validatePasswordUseCase(password)

        //Then: The result should be PASSWORD_TOO_SHORT error
        assertEquals(Result.Error(PasswordError.PASSWORD_TOO_SHORT), result)
    }

    @Test
    fun `should return Success when password is valid`() {
        //Given: A valid password
        val password = "validpassword"

        //When: The validatePasswordUseCase is invoked
        val result = validatePasswordUseCase(password)

        //Then: The result should be Success
        assertEquals(Result.Success(Unit), result)
    }
}
