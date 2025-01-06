package com.example.newssubscription.news.presentation.bookmark

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.domain.usecase.GetBookMarkedArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getArticlesUseCase: GetBookMarkedArticlesUseCase,
) : ViewModel() {
   private val _articles = MutableStateFlow(emptyList<Article>())
    val articles = _articles.asStateFlow()

    init {
        viewModelScope.launch {
            getArticlesUseCase().collectLatest {
                _articles.value = it
            }
        }
    }
}