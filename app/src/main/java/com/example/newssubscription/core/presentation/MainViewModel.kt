package com.example.newssubscription.core.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.app.navigation.Routes
import com.example.newssubscription.core.domain.usecase.IsUserSignedInUseCase
import com.example.newssubscription.core.domain.usecase.SignOutUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private var _keepOnScreenCondition: MutableState<Boolean> = mutableStateOf(true)
    val keepOnScreenCondition: State<Boolean> = _keepOnScreenCondition

    private val _startDestination: MutableState<Routes> = mutableStateOf(Routes.NewsAuthentication)
    val startDestination: State<Routes> = _startDestination

    init {
        viewModelScope.launch {
            _startDestination.value =
                if (!isUserSignedInUseCase()) Routes.NewsAuthentication else Routes.NewsNavigation
            delay(600) //Without this delay, the onBoarding screen will show for a momentum.
            _keepOnScreenCondition.value = false
        }
    }

    fun signOut() {
        signOutUseCase()
        _startDestination.value = Routes.NewsAuthentication
    }
}