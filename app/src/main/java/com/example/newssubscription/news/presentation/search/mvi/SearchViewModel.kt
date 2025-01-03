package com.example.news.search.presentation.mvi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newssubscription.news.domain.usecase.SearchForNewsUseCase
import com.example.newssubscription.news.presentation.search.mvi.SearchIntent
import com.example.newssubscription.news.presentation.search.mvi.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchForNewsUseCase: SearchForNewsUseCase,
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set


    fun onIntent(intent: SearchIntent) {
        when (intent) {
            SearchIntent.SearchNews -> searchNews()
            is SearchIntent.UpdateSearchQuery -> state =
                state.copy(searchQuery = intent.searchQuery)
        }
    }


    private fun searchNews() {
        val articles = searchForNewsUseCase(
            sources = listOf("bbc-news", "abc-news", "al-jazeera-english"),
            searchQuery = state.searchQuery
        ).cachedIn(viewModelScope)
        state = state.copy(articles = articles)
    }

}