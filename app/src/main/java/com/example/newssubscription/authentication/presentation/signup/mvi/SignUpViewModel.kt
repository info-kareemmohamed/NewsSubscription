package com.example.newssubscription.authentication.presentation.signup.mvi

import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.authentication.domain.repository.AuthRepository
import com.example.newssubscription.authentication.domain.usecase.ValidateConfirmPasswordUseCase
import com.example.newssubscription.authentication.domain.usecase.ValidateEmailUseCase
import com.example.newssubscription.authentication.domain.usecase.ValidatePasswordUseCase
import com.example.newssubscription.authentication.domain.usecase.ValidateUserNameUseCase
import com.example.newssubscription.authentication.domain.util.AuthError
import com.example.newssubscription.authentication.domain.util.ValidationError
import com.example.newssubscription.authentication.domain.util.ValidationError.EmailError
import com.example.newssubscription.authentication.domain.util.ValidationError.PasswordError
import com.example.newssubscription.authentication.domain.util.ValidationError.UsernameError
import com.example.newssubscription.core.domain.util.Result
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
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateUserNameUseCase: ValidateUserNameUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase
) : ViewModel() {

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    private val _signUpSuccessfully = Channel<Boolean>()
    val signUpSuccessfully = _signUpSuccessfully.receiveAsFlow()

    private val _googleSignUpSuccessfully = Channel<Boolean>()
    val googleSignUpSuccessfully = _googleSignUpSuccessfully.receiveAsFlow()

    fun onIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.ConfirmPasswordChanged -> _signUpState.update {
                it.copy(
                    confirmPassword = intent.confirmPassword,
                    confirmPasswordErrorMessage =
                    validateConfirmPassword(intent.confirmPassword, password = it.password)
                )
            }

            is SignUpIntent.EmailChanged -> {
                val emailTrim = intent.email.trim()
                _signUpState.update {
                    it.copy(email = emailTrim, emailErrorMessage = validateEmail(emailTrim))
                }
            }

            is SignUpIntent.PasswordChanged -> _signUpState.update {
                it.copy(
                    password = intent.password,
                    passwordErrorMessage = validatePassword(intent.password)
                )
            }

            is SignUpIntent.UsernameChanged -> _signUpState.update {
                it.copy(
                    username = intent.username,
                    usernameErrorMessage = validateUsername(intent.username)
                )
            }

            is SignUpIntent.ConfirmPasswordVisibilityChanged -> _signUpState.update {
                it.copy(isConfirmPasswordVisible = intent.isVisible)
            }

            is SignUpIntent.PasswordVisibilityChanged -> _signUpState.update {
                it.copy(isPasswordVisible = intent.isVisible)
            }

            SignUpIntent.Submit -> submit()

            is SignUpIntent.GoogleSignInClicked -> googleSignIn(intent.response)
        }
    }


    private fun checkAllFields(): Boolean {
        _signUpState.update {
            it.copy(
                emailErrorMessage = validateEmail(it.email),
                passwordErrorMessage = validatePassword(it.password),
                confirmPasswordErrorMessage = validateConfirmPassword(
                    it.confirmPassword,
                    it.password
                ),
                usernameErrorMessage = validateUsername(it.username)
            )
        }

        val hasError = listOf(
            _signUpState.value.emailErrorMessage,
            _signUpState.value.passwordErrorMessage,
            _signUpState.value.confirmPasswordErrorMessage,
            _signUpState.value.usernameErrorMessage
        ).any { it != null }

        return hasError
    }


    private fun submit() {
        if (checkAllFields()) return

        viewModelScope.launch {
            val state = _signUpState.value
            _signUpState.value = state.copy(isLoading = true)

            authRepository.signUp(state.username, state.email, state.password)
                .onSuccess {
                    _signUpState.value = state.copy(isLoading = false)
                    _signUpSuccessfully.send(true)
                }
                .onError { updateStateForError(it) }
        }
    }

    private fun googleSignIn(credential: GetCredentialResponse) {
        viewModelScope.launch {
            _signUpState.update { it.copy(isLoadingGoogle = true, errorMessage = null) }

            authRepository.googleSignIn(credential)
                .onSuccess {
                    _signUpState.update { it.copy(isLoadingGoogle = false) }
                    _googleSignUpSuccessfully.send(true)
                }
                .onError { error -> updateStateForError(error) }
        }

    }


    private fun updateStateForError(error: AuthError) {
        _signUpState.update {
            it.copy(
                errorMessage = error,
                isLoading = false,
                isLoadingGoogle = false
            )
        }
    }


    private fun validateEmail(email: String) =
        validateInput(email) { validateEmailUseCase(it[0]) } as? EmailError

    private fun validatePassword(password: String) =
        validateInput(password) { validatePasswordUseCase(it[0]) } as? PasswordError

    private fun validateUsername(username: String) =
        validateInput(username) { validateUserNameUseCase(it[0]) } as? UsernameError

    private fun validateConfirmPassword(confirmPassword: String, password: String) =
        validateInput(password, confirmPassword) { validateConfirmPasswordUseCase(it[0], it[1]) }
                as? PasswordError

    //To handle validation errors
    private inline fun validateInput(
        vararg inputs: String,
        useCase: (Array<out String>) -> Result<Unit, ValidationError>
    ): ValidationError? {
        return when (val result = useCase(inputs)) {
            is Result.Success -> null
            is Result.Error -> result.error
        }
    }

}
