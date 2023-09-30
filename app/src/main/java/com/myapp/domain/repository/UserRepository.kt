package com.myapp.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.myapp.presentation.util.ResultHappen

interface UserRepository {

    suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        surname: String,
        profileImage: String
    ): ResultHappen<FirebaseUser?>

    suspend fun loginUser(email: String, password: String): ResultHappen<FirebaseUser?>

    suspend fun getCurrentUserDetails(): HashMap<String, String?>

    suspend fun updateUserDetails(
        name: String,
        surname: String,
        email: String,
        profileImage: String
    ): ResultHappen<Unit>
}
