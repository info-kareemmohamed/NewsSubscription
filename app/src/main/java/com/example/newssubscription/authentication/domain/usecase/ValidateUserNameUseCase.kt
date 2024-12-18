package com.example.newssubscription.authentication.domain.usecase

import com.example.newssubscription.authentication.domain.util.ValidationError.UsernameError
import com.example.newssubscription.core.domain.util.Result
import javax.inject.Inject

class ValidateUserNameUseCase @Inject constructor() {

    private val usernameRegex = "^[A-Za-z\\s]+\$".toRegex()

    operator fun invoke(username: String): Result<Unit, UsernameError> =
        when {
            username.isBlank() -> Result.Error(UsernameError.USERNAME_EMPTY)
            username.length < 3 -> Result.Error(UsernameError.USERNAME_TOO_SHORT)
            username.length > 20 -> Result.Error(UsernameError.USERNAME_TOO_LONG)
            !username.matches(usernameRegex) -> Result.Error(UsernameError.USERNAME_INVALID_FORMAT)
            else -> Result.Success(Unit)
        }
}
