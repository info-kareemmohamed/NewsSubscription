package com.example.newssubscription.onboarding.presentation.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.core.domain.repository.LocalUserAppEntry
import com.example.newssubscription.onboarding.data.Page
import com.example.newssubscription.onboarding.data.PageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val pageData: PageData,
    private val localUserAppEntry: LocalUserAppEntry
) : ViewModel() {


    var currentPage: Int by mutableIntStateOf(0)
        private set
    var pages: List<Page> by mutableStateOf(emptyList())
        private set
    var isLastPage: Boolean by mutableStateOf(false)
        private set

    init {
        loadPages()
    }


    private fun loadPages() {
        pages = pageData.pages
    }

    fun saveAppEntry() {
        viewModelScope.launch {
            localUserAppEntry.saveAppEntry()
        }
    }

    fun changePage(value: Int) {
        val newPage = (value).coerceIn(0, pageData.pages.size - 1)
        isLastPage = newPage == pageData.pages.size - 1
        currentPage = newPage
    }
}


