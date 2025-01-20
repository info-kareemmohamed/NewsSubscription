package com.example.newssubscription.payment.data.repository

import com.example.newssubscription.payment.domain.repository.CurrentTime
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class CurrentTimeImpl @Inject constructor(
    firestore: FirebaseFirestore
) : CurrentTime {
    private val currentTimeDoc = firestore.collection("App").document("current_time")


    override suspend fun getCurrentTime(): ZonedDateTime? {

        currentTimeDoc.set(
            mapOf(
                "currentTime" to FieldValue.serverTimestamp()
            )
        ).await()

        val document = currentTimeDoc.get().await()
        val timestamp = document.getTimestamp("currentTime")

        return if (timestamp != null) {
            val currentDate = timestamp.toDate()
            currentDate.toInstant().atZone(ZoneId.systemDefault())
        } else {
            null
        }
    }
}
