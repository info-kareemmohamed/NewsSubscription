package com.example.newssubscription.authentication.domain.usecase

import com.example.newssubscription.authentication.domain.util.ValidationError.EmailError
import com.example.newssubscription.core.domain.util.Result
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {

    private val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()

    operator fun invoke(email: String): Result<Unit, EmailError> =
        when {
            email.isBlank() -> Result.Error(EmailError.EMAIL_EMPTY)
            !emailPattern.matches(email) -> Result.Error(EmailError.EMAIL_INVALID_FORMAT)
            else -> Result.Success(Unit)
        }
}
