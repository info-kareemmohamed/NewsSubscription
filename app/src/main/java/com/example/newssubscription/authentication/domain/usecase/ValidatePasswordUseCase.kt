package com.example.newssubscription.authentication.domain.usecase

import com.example.newssubscription.authentication.domain.util.ValidationError.PasswordError
import com.example.newssubscription.core.domain.util.Result
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {

    operator fun invoke(password: String): Result<Unit, PasswordError> =
        when {
            password.isBlank() -> Result.Error(PasswordError.PASSWORD_EMPTY)
            password.length < 6 -> Result.Error(PasswordError.PASSWORD_TOO_SHORT)
            else -> Result.Success(Unit)
        }

}