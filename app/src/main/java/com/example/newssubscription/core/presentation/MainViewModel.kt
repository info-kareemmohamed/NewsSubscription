package com.example.newssubscription.core.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.app.navigation.Routes
import com.example.newssubscription.core.domain.repository.LocalUserAppEntry
import com.example.newssubscription.core.domain.repository.UserRepository
import com.example.newssubscription.core.domain.usecase.CanUserReadArticleUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainViewModel @Inject constructor(
    val canUserReadArticleUseCase: CanUserReadArticleUseCase,
    private val userRepository: UserRepository,
    localUserAppEntry: LocalUserAppEntry,
) : ViewModel() {

    private var _keepOnScreenCondition: MutableState<Boolean> = mutableStateOf(true)
    val keepOnScreenCondition: State<Boolean> = _keepOnScreenCondition

    private val _startDestination: MutableState<Routes> = mutableStateOf(Routes.NewsAuthentication)
    val startDestination: State<Routes> = _startDestination


    init {
        localUserAppEntry.readAppEntry().onEach { startFromNews ->
            setStartDestination(startFromNews)
        }.launchIn(viewModelScope)
    }


    private suspend fun setStartDestination(startFromNews: Boolean) {
        if (startFromNews) {
            _startDestination.value =
                if (userRepository.isUserSignedIn()) Routes.NewsNavigation else Routes.NewsAuthentication
        } else {
            _startDestination.value = Routes.AppStartNavigation
        }
        delay(600) //Without this delay, the onBoarding screen will show for a momentum.
        _keepOnScreenCondition.value = false
    }

}