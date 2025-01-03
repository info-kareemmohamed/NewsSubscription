package com.example.newssubscription.news.domain.usecase

import com.example.newssubscription.news.domain.model.Article
import com.example.newssubscription.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookMarkedArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(): Flow<List<Article>> = newsRepository.getBookMarkedArticles()
}