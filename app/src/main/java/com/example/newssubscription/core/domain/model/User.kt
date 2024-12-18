package com.example.news.core.domain.model

import java.util.Date

data class User(
    val id: String="",
    val name: String="",
    val email: String="",
    val premium:Boolean=false,
    val visitedArticleUrls: List<String> = emptyList(),
    val startPremiumDate: Date?=null,
    val expirationPremiumDate: Date?=null,
    val subscriptionMonths:Int=0,
    val hasFreeRead:Boolean=true,
    val articlesShownCount:Int=0,
    val startFreeReadDate: Date?=null,
    val profilePictureUrl: String? = null
)