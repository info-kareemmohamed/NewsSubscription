package com.example.newssubscription.news.data.repository

import com.example.newssubscription.news.domain.repository.PhotoRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.storage
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
) : PhotoRepository {

    override suspend fun uploadPhoto(
        email: String?, previousPhotoUrl: String?, photo: ByteArray
    ): String {
        val bucket = supabaseClient.storage["profile"]
        previousPhotoUrl?.let { deletePreviousPhoto(it, bucket) }// Delete previous photo if exists
        val fileName = "${email}${System.currentTimeMillis()}.png"
        val uploadedPath = bucket.upload(fileName, photo) { upsert = true }.path
        return generatePublicUrl(uploadedPath)
    }

    private suspend fun deletePreviousPhoto(photoUrl: String, bucket: BucketApi) {
        val fileName = photoUrl.substringAfterLast("/")
        bucket.delete(fileName)
    }

    private fun generatePublicUrl(fileName: String): String {
        return "https://mpcgeqchkodeonrciirt.supabase.co/storage/v1/object/public/profile/$fileName"
    }
}