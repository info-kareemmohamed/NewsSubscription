package com.example.newssubscription.payment.presentation.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.core.domain.model.User
import com.example.newssubscription.core.domain.repository.UserRepository
import com.example.newssubscription.core.domain.util.onError
import com.example.newssubscription.core.domain.util.onSuccess
import com.example.newssubscription.payment.domain.repository.CurrentTime
import com.example.newssubscription.payment.domain.repository.PaymentRepository
import com.example.newssubscription.payment.domain.usecase.UpgradeToPremiumUseCase
import com.example.newssubscription.payment.domain.util.PaymentError
import com.paymob.paymob_sdk.ui.PaymobSdkListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
    private val upgradeToPremiumUseCase: UpgradeToPremiumUseCase,
    private val currentTime: CurrentTime
) : ViewModel(), PaymobSdkListener {

    private val _state = MutableStateFlow(PaymentState())
    val state = _state.asStateFlow()

    private val _clientSecret = Channel<String>()
    val clientSecret = _clientSecret.receiveAsFlow()

    private val _finishPayment = Channel<Boolean>()
    val finishPayment = _finishPayment.receiveAsFlow()

    private val _errorMessage = Channel<PaymentError>()
    val errorMessage = _errorMessage.receiveAsFlow()


    init {
        viewModelScope.launch {
            userRepository.getAuthenticatedUserAsFlow().collect { user ->
                _state.update { it.copy(user = user) }
            }
        }
    }

    fun startPayment(amount: Float, subscriptionMonths: Int) {
        _state.value.user?.let { user ->
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                paymentRepository.getClientSecret(
                    amount = amount,
                    userName = user.name,
                    userEmail = user.email,
                    subscriptionMonths = subscriptionMonths
                ).onSuccess { clientSecret: String ->
                    _state.update { it.copy(subscriptionMonths = subscriptionMonths) }
                    _clientSecret.send(clientSecret)
                }.onError { error -> _errorMessage.send(error) }
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun updateUserSubscription() {
        _state.value.user?.let { user ->
            viewModelScope.launch {
                currentTime.getCurrentTime()?.let { currentTime ->
                    upgradeToPremiumUseCase(user, currentTime, _state.value.subscriptionMonths)
                    _finishPayment.send(true)
                }
            }
        }
    }

    // PaymobSdkListener implementations
    override fun onFailure() {
    }

    override fun onPending() {
    }

    override fun onSuccess(payResponse: HashMap<String, String?>) {
        updateUserSubscription()
    }

}

data class PaymentState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val subscriptionMonths: Int = 0,
)
