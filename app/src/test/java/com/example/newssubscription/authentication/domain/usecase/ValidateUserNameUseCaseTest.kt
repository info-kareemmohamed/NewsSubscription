package com.example.newssubscription.authentication.domain.usecase

import com.example.newssubscription.core.domain.util.Result
import com.example.newssubscription.authentication.domain.util.ValidationError.UsernameError

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateUserNameUseCaseTest {

    private lateinit var validateUserNameUseCase: ValidateUserNameUseCase

    @Before
    fun setUp() {
        validateUserNameUseCase = ValidateUserNameUseCase()
    }

    @Test
    fun `should return USERNAME_EMPTY error when username is blank`() {
        //Given: A blank username
        val username = ""

        //When: The validateUserNameUseCase is invoked
        val result = validateUserNameUseCase(username)

        //Then: The result should be USERNAME_EMPTY error
        assertEquals(Result.Error(UsernameError.USERNAME_EMPTY), result)
    }

    @Test
    fun `should return USERNAME_TOO_SHORT error when username length is less than 3`() {
        //Given: A username with length less than 3
        val username = "ab"

        //When: The validateUserNameUseCase is invoked
        val result = validateUserNameUseCase(username)

        //Then: The result should be USERNAME_TOO_SHORT error
        assertEquals(Result.Error(UsernameError.USERNAME_TOO_SHORT), result)
    }

    @Test
    fun `should return USERNAME_TOO_LONG error when username length is more than 20`() {
        //Given: A username with length more than 20
        val username = "a".repeat(21)

        //When: The validateUserNameUseCase is invoked
        val result = validateUserNameUseCase(username)

        //Then: The result should be USERNAME_TOO_LONG error
        assertEquals(Result.Error(UsernameError.USERNAME_TOO_LONG), result)
    }

    @Test
    fun `should return USERNAME_INVALID_FORMAT error when username contains invalid characters`() {
        //Given: A username with invalid characters
        val username = "user@name"

        //When: The validateUserNameUseCase is invoked
        val result = validateUserNameUseCase(username)

        //Then: The result should be USERNAME_INVALID_FORMAT error
        assertEquals(Result.Error(UsernameError.USERNAME_INVALID_FORMAT), result)
    }

    @Test
    fun `should return Success when username is valid`() {
        //Given: A valid username
        val username = "valid username"

        //When: The validateUserNameUseCase is invoked
        val result = validateUserNameUseCase(username)

        //Then: The result should be Success
        assertEquals(Result.Success(Unit), result)
    }
}