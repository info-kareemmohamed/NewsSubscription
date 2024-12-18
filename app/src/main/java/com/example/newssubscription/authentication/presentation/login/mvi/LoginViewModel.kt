package com.example.newssubscription.authentication.presentation.login.mvi

import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.authentication.domain.repository.AuthRepository
import com.example.newssubscription.authentication.domain.usecase.ValidateEmailUseCase
import com.example.newssubscription.authentication.domain.usecase.ValidatePasswordUseCase
import com.example.newssubscription.authentication.domain.util.AuthError
import com.example.newssubscription.core.domain.util.onError
import com.example.newssubscription.core.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _loginSuccessfully = Channel<Boolean>()
    val loginSuccessfully = _loginSuccessfully.receiveAsFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> validateEmail(intent.email)
            is LoginIntent.PasswordChanged -> validatePassword(intent.password)

            is LoginIntent.VisibilityChanged -> _loginState.update { it.copy(isPasswordVisible = intent.isVisible) }
            is LoginIntent.RememberMeChanged -> _loginState.update { it.copy(isRememberMeChecked = intent.isChecked) }

            LoginIntent.LoginClicked -> {
                val isValidEmail = validateEmail(_loginState.value.email)
                val isValidPassword = validatePassword(_loginState.value.password)
                if (isValidEmail && isValidPassword) login()
            }

            is LoginIntent.GoogleSignInClicked -> googleSignIn(intent.response)
            LoginIntent.ForgotPasswordClicked -> if (validateEmail(_loginState.value.email)) forgotPassword()
        }
    }


    private fun forgotPassword() {
        viewModelScope.launch {

            _loginState.update {
                it.copy(isLoading = true, errorMessage = null, forgotPasswordSuccessMessage = false)
            }
            authRepository.forgotPassword(_loginState.value.email)
                .onSuccess {
                    _loginState.update {
                        it.copy(isLoading = false, forgotPasswordSuccessMessage = true)
                    }
                }.onError { updateStateForError(it) }
        }
    }


    private fun login() {
        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, errorMessage = null) }

            authRepository.signIn(_loginState.value.email, _loginState.value.password)
                .onSuccess {
                    _loginState.update { it.copy(isLoading = false) }; _loginSuccessfully.send(true)
                }.onError { error -> updateStateForError(error) }
        }
    }


    private fun googleSignIn(credential: GetCredentialResponse) {
        viewModelScope.launch {
            _loginState.update { it.copy(isLoadingGoogle = true, errorMessage = null) }

            authRepository.googleSignIn(credential)
                .onSuccess {
                    _loginState.update { it.copy(isLoadingGoogle = false) }
                    _loginSuccessfully.send(true)
                }.onError { error -> updateStateForError(error) }
        }
    }


    //To handle validation errors
    private fun validateEmail(email: String): Boolean {
        //add new email and reset error
        _loginState.update { it.copy(email = email, emailErrorMessage = null) }

        validateEmailUseCase(email.trim()).onError { error ->
            _loginState.update { it.copy(emailErrorMessage = error) }; return@validateEmail false
        }
        return true
    }

    //To handle validation errors
    private fun validatePassword(password: String): Boolean {
        //add new password and reset error
        _loginState.update { it.copy(password = password, passwordErrorMessage = null) }

        validatePasswordUseCase(password).onError { error ->
            _loginState.update { it.copy(passwordErrorMessage = error) }; return@validatePassword false
        }
        return true
    }


    private fun updateStateForError(error: AuthError) {
        _loginState.update {
            it.copy(
                errorMessage = error,
                isLoading = false,
                isLoadingGoogle = false
            )
        }
    }

}
