package com.example.newssubscription.authentication.data


import androidx.credentials.GetCredentialResponse
import com.example.news.core.domain.model.User
import com.example.newssubscription.authentication.domain.repository.AuthRepository
import com.example.newssubscription.authentication.domain.util.AuthError
import com.example.newssubscription.core.domain.util.Result
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AuthRepository {

    private val collectionName = "users"

    override suspend fun signIn(email: String, password: String): Result<User, AuthError> =
        handleAuthOperation {
            val userId = firebaseAuth.signInWithEmailAndPassword(email, password).await().user?.uid
            val user = userId?.let { getUserFromFirestore(it) }
            user?.let { Result.Success(it) } ?: Result.Error(AuthError.USER_DISABLED)
        }


    override suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): Result<User, AuthError> = handleAuthOperation {
        val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user?.uid
            ?.let {
                User(id = it, email = email, name = username)
                    .apply { saveUserToFirestore(this, this.id) }
            }
        user?.let { Result.Success(it) } ?: Result.Error(AuthError.UNKNOWN_ERROR)
    }


    override suspend fun forgotPassword(email: String): Result<Unit, AuthError> =
        handleAuthOperation {
            searchUserByEmail(email)?.let {
                firebaseAuth.sendPasswordResetEmail(email).await()
                Result.Success(Unit)
            } ?: Result.Error(AuthError.EMAIL_NOT_FOUND)
        }


    override suspend fun googleSignIn(result: GetCredentialResponse): Result<User, AuthError> =
        handleAuthOperation {
            val authCredential = GoogleAuthProvider.getCredential(
                GoogleIdTokenCredential.createFrom(result.credential.data).idToken, null
            )

            val authResult = firebaseAuth.signInWithCredential(authCredential).await()
            val firebaseUser =
                authResult.user ?: return@handleAuthOperation Result.Error(AuthError.USER_NOT_FOUND)

            val user = getUserFromFirestore(firebaseUser.uid) ?: User(
                id = firebaseUser.uid,
                email = firebaseUser.email.orEmpty(),
                name = firebaseUser.displayName.orEmpty()
            ).also { saveUserToFirestore(it, firebaseUser.uid) }

            Result.Success(user)
        }


    override suspend fun getSignedInUser(): User? {
        val uid = firebaseAuth.currentUser?.uid ?: return null
        return getUserFromFirestore(uid)
    }


    override fun signOut() {
        firebaseAuth.signOut()
    }


    private suspend fun <T> handleAuthOperation(operation: suspend () -> Result<T, AuthError>): Result<T, AuthError> =
        try {
            operation()
        } catch (e: Exception) {
            e.printStackTrace()
            coroutineContext.ensureActive()
            Result.Error(mapFirebaseError(e))
        }


    private suspend fun getUserFromFirestore(uid: String): User? {
        return firestore.collection(collectionName).document(uid).get().await()
            .toObject(User::class.java)
    }


    private suspend fun saveUserToFirestore(user: User, uid: String): User? {
        firestore.collection(collectionName).document(uid).set(user).await()
        return getUserFromFirestore(uid)
    }


    private suspend fun searchUserByEmail(email: String): User? {
        val querySnapshot = firestore.collection(collectionName)
            .whereEqualTo("email", email)
            .get()
            .await()
        return querySnapshot.documents.firstOrNull()?.toObject(User::class.java)
    }


}
