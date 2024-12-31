package com.example.newssubscription.news.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.domain.usecase.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getNewsUseCase: GetNewsUseCase) : ViewModel() {


    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> get() = _state

    init {
        loadArticles()
    }

    // Load articles from the use case
    fun loadArticles() {
        _state.value = _state.value.copy(isLoading = true)
        _state.value = _state.value.copy(
            articles = getNewsUseCase(
                sources = listOf("bbc-news", "abc-news", "al-jazeera-english")
            ).cachedIn(viewModelScope)
        )
        _state.value = _state.value.copy(isLoading = false)
    }
}


data class HomeState(
    val articles: Flow<PagingData<Article>>? = null,
    val isLoading: Boolean = false,
)