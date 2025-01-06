package com.example.newssubscription.news.domain.usecase

import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.domain.repository.NewsRepository
import javax.inject.Inject

class ToggleOrInsertBookmarkUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(article: Article) = newsRepository.toggleOrInsertBookmark(article)
}