package com.myapp.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.myapp.data.model.UserModel
import com.myapp.presentation.util.ResultHappen

interface UserRepository {

    suspend fun registerUser(userModel: UserModel): ResultHappen<FirebaseUser?>

    suspend fun loginUser(email: String, password: String): ResultHappen<FirebaseUser?>

    suspend fun getCurrentUserDetails(): HashMap<String, String?>

    suspend fun updateUserDetails(userModel: UserModel): ResultHappen<Unit>
}
