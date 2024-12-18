package com.example.newssubscription.authentication.data

import com.example.newssubscription.authentication.domain.util.AuthError
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestoreException


//To map Firebase exceptions to our custom AuthError
 fun mapFirebaseError(exception: Throwable): AuthError {
    return when (exception) {
        is FirebaseAuthInvalidCredentialsException -> AuthError.INVALID_CREDENTIALS
        is FirebaseAuthInvalidUserException -> AuthError.USER_NOT_FOUND
        is FirebaseAuthUserCollisionException -> AuthError.EMAIL_ALREADY_EXISTS
        is FirebaseAuthException -> AuthError.UNKNOWN_ERROR
        is FirebaseNetworkException -> AuthError.NETWORK_ERROR

        // Handling Firestore exceptions
        is FirebaseFirestoreException -> {
            when (exception.code) {
                FirebaseFirestoreException.Code.UNAVAILABLE -> AuthError.NETWORK_ERROR
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> AuthError.INSUFFICIENT_PERMISSION
                FirebaseFirestoreException.Code.NOT_FOUND -> AuthError.USER_NOT_FOUND
                else -> AuthError.UNKNOWN_ERROR
            }
        }

        is IllegalArgumentException -> AuthError.INVALID_ARGUMENT
        else -> AuthError.UNKNOWN_ERROR
    }
}