package com.example.newssubscription.authentication.domain.usecase

import com.example.newssubscription.authentication.domain.util.ValidationError.PasswordError
import com.example.newssubscription.core.domain.util.Result
import javax.inject.Inject

class ValidateConfirmPasswordUseCase @Inject constructor() {

    operator fun invoke(password: String, confirmPassword: String): Result<Unit, PasswordError> =
        when {
            confirmPassword.isBlank() -> Result.Error(PasswordError.PASSWORD_EMPTY)
            password != confirmPassword -> Result.Error(PasswordError.CONFIRM_PASSWORD_DOES_NOT_MATCH)
            else -> Result.Success(Unit)
        }

}