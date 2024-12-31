package com.example.newssubscription.news.domain.usecase

import com.example.newssubscription.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(sources: List<String>) = newsRepository.getNews(sources)

}