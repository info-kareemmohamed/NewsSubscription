package com.example.newssubscription.core.data.repository


import com.example.news.core.domain.model.User
import com.example.newssubscription.core.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : UserRepository {

    private val collectionName = "users"

    override fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun updateUser(user: User?) {
        user?.let {
            firestore.collection(collectionName).document(user.id).set(user).await()
        }
    }

    override fun getAuthenticatedUserAsFlow(): Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            auth.currentUser?.let { user ->
                firestore.collection(collectionName)
                    .document(user.uid)
                    .addSnapshotListener { snapshot, error ->
                        error?.let {
                            close(it)
                        } ?: trySend(snapshot?.toObject(User::class.java))
                    }
            } ?: trySend(null)
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun getUser(): User? {
        val uid = firebaseAuth.currentUser?.uid ?: return null
        return firestore.collection(collectionName).document(uid).get().await()
            .toObject(User::class.java)
    }
}