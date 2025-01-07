package com.example.newssubscription.news.domain.repository

interface PhotoRepository {
    suspend fun uploadPhoto(email:String?,previousPhotoUrl:String?,photo: ByteArray):String
}
