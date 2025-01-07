package com.example.newssubscription.news.presentation.settings.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.core.domain.repository.UserRepository
import com.example.newssubscription.news.domain.repository.PhotoRepository
import com.example.newssubscription.news.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val photoRepository: PhotoRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            userRepository.getAuthenticatedUserAsFlow().collect { user ->
                _state.value = state.value.copy(user = user)
            }
        }
    }

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.OnLogoutClick -> signOutUseCase()
            is SettingsIntent.OnImageChange -> handleImageChange(intent.byteArray)
        }
    }

    private fun handleImageChange(byteArray: ByteArray?) {
        viewModelScope.launch {
            try {
                byteArray?.let { photoByteArray ->
                    _state.update { it.copy(imageLoading = true) }

                    val newPhotoUrl =
                        photoRepository.uploadPhoto(
                            state.value.user?.email,
                            state.value.user?.profilePictureUrl, photoByteArray
                        )
                    _state.update { it.copy(user = it.user?.copy(profilePictureUrl = newPhotoUrl)) }
                    userRepository.updateUser(state.value.user)

                    _state.update { it.copy(imageLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(imageLoading = false, error = e.message) }
            }
        }
    }
}