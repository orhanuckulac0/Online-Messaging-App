package com.myapp.domain

import com.google.firebase.auth.FirebaseUser
import com.myapp.presentation.firebase.FirebaseAuthManager

class RegisterUserUseCase(private val firebaseAuthManager: FirebaseAuthManager) {
    fun execute(
        email: String,
        password: String,
        name: String,
        surname: String,
        profileImage: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuthManager.registerUser(
            email = email,
            password = password,
            name = name,
            surname = surname,
            profileImage= profileImage,
            onComplete,
            onError
        )
    }
}
