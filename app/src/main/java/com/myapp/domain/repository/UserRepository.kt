package com.myapp.domain.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.myapp.data.model.UserModel
import com.myapp.presentation.util.ResultHappen

interface UserRepository {

    suspend fun registerUser(userModel: UserModel): ResultHappen<FirebaseUser?>

    suspend fun loginUser(email: String, password: String): ResultHappen<FirebaseUser?>

    suspend fun getCurrentUserDetails(): ResultHappen<UserModel>

    suspend fun updateUserDetails(userModel: UserModel): ResultHappen<Unit>

    suspend fun getImageURLFromStorage(uri: Uri, context: Context): ResultHappen<String?>
}
