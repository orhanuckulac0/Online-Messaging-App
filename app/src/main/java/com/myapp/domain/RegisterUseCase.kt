package com.myapp.domain

import com.google.firebase.auth.FirebaseUser
import com.myapp.presentation.firebase.FirebaseAuthManager

class RegisterUserUseCase(private val firebaseAuthManager: FirebaseAuthManager) {
    fun execute(
        email: String,
        password: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuthManager.registerUser(
            email = email,
            password = password,
            onComplete,
            onError
        )
    }
}
