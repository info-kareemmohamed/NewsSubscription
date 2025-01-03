package com.example.newssubscription.news.presentation.bookmark

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.domain.usecase.GetBookMarkedArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getArticlesUseCase: GetBookMarkedArticlesUseCase,
) : ViewModel() {
    val articles = mutableStateOf(emptyList<Article>())

    init {
        viewModelScope.launch {
            getArticlesUseCase().collect {
                articles.value = it
            }
        }
    }
}