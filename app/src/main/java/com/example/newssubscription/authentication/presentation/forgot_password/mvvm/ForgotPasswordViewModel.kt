package com.example.newssubscription.authentication.presentation.forgot_password.mvvm

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.authentication.domain.repository.AuthRepository
import com.example.newssubscription.authentication.presentation.util.asUiText
import com.example.newssubscription.core.domain.util.onError
import com.example.newssubscription.core.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: com.example.newssubscription.authentication.domain.usecase.ValidateEmailUseCase,
    private val application: Application
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
        validateEmail()
    }

    private fun validateEmail(): Boolean {
        var error: String? = null
        validateEmailUseCase(email).onError {
            error = it.asUiText(application.applicationContext)
        }
        return error != null
    }

    fun forgotPassword() {
        if (!validateEmail()) return

        viewModelScope.launch {
            isLoading = true
            authRepository.forgotPassword(email).onSuccess {
                errorMessage = null
                isLoading = false
            }.onError {
                errorMessage = it.asUiText(application.applicationContext)
                isLoading = false
            }

        }
    }
}
