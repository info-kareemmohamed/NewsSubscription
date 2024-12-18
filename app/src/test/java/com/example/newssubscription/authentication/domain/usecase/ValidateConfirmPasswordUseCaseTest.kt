package com.example.newssubscription.authentication.domain.usecase

import com.example.newssubscription.core.domain.util.Result
import com.example.newssubscription.authentication.domain.util.ValidationError.PasswordError
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ValidateConfirmPasswordUseCaseTest {
    private lateinit var validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase


    @Before
    fun setUp() {
        validateConfirmPasswordUseCase = ValidateConfirmPasswordUseCase()
    }

    @Test
    fun `should return CONFIRM_PASSWORD_DOES_NOT_MATCH when password and confirmPassword are different`() {
        //Given: Two passwords that are different
        val password = "password"
        val confirmPassword = "differentPassword"

        //When: The validateConfirmPasswordUseCase is invoked
        val result = validateConfirmPasswordUseCase(password, confirmPassword)

        //Then: The result should be CONFIRM_PASSWORD_DOES_NOT_MATCH error
        assertEquals(Result.Error(PasswordError.CONFIRM_PASSWORD_DOES_NOT_MATCH), result)
    }


    @Test
    fun `should return Success when password and confirmPassword are the same`() {
        //Given: Two passwords that are the same
        val password = "password"
        val confirmPassword = "password"

        //When: The validateConfirmPasswordUseCase is invoked
        val result = validateConfirmPasswordUseCase(password, confirmPassword)

        //Then: The result should be Success
        assertEquals(Result.Success(Unit), result)
    }


    @Test
    fun `should return PasswordError_PASSWORD_EMPTY when confirmPassword is empty`() {
        //Given: Two passwords that are empty
        val password = ""
        val confirmPassword = ""

        //When: The validateConfirmPasswordUseCase is invoked
        val result = validateConfirmPasswordUseCase(password, confirmPassword)

        //Then: The result should be PasswordError_PASSWORD_EMPTY
        assertEquals(Result.Error(PasswordError.PASSWORD_EMPTY), result)
    }
}