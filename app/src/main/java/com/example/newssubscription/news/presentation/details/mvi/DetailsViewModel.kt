package com.example.newssubscription.news.presentation.details.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.domain.usecase.ToggleOrInsertBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val toggleOrInsertBookmarkUseCase: ToggleOrInsertBookmarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    fun onIntent(intent: DetailsIntent) {
        when (intent) {
            DetailsIntent.OnBookmarkClick -> {
                _state.update {
                    it.copy(isBookmarked = !state.value.isBookmarked)
                }
            }

            is DetailsIntent.LoadArticle -> loadArticle(intent.article)
            DetailsIntent.SaveFinalBookmarkInData -> saveFinalBookmark()
        }
    }


    private fun saveFinalBookmark() {
        _state.value.article?.takeIf { it.isBookMarked != _state.value.isBookmarked }
            ?.let { article ->
                viewModelScope.launch {
                    toggleOrInsertBookmarkUseCase(article)// Toggle the bookmark status
                }
            }
    }

    private fun loadArticle(article: Article) {
        viewModelScope.launch {
            _state.update {
                it.copy(article = article, isBookmarked = article.isBookMarked)
            }
        }
    }
}