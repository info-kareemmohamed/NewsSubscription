package com.example.newssubscription.news.data.mappers

import com.example.newssubscription.news.data.local.ArticleEntity
import com.example.newssubscription.news.data.remote.dto.ArticleDto
import com.example.newssubscription.news.domain.model.Article


fun ArticleDto.toArticle(): Article =
    Article(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        sourceName = source.name,
        title = title,
        url = url,
        imageUrl = urlToImage,
    )


fun ArticleDto.toArticleEntity(isBookMarked: Boolean, isCached: Boolean): ArticleEntity =
    ArticleEntity(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        sourceName = source.name,
        title = title,
        url = url,
        imageUrl = urlToImage,
        isBookMarked = isBookMarked,
        isCached = isCached
    )

fun ArticleEntity.toArticle(): Article =
    Article(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        sourceName = sourceName,
        title = title,
        url = url,
        imageUrl = imageUrl,
    )

