package com.myapp.domain.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.myapp.data.model.PushNotification
import com.myapp.data.model.UserModel
import com.myapp.data.model.UserModelFirestore
import com.myapp.presentation.util.ResultHappen

interface UserRepository {

    suspend fun registerUser(userModel: UserModel): ResultHappen<FirebaseUser?>
    suspend fun loginUser(email: String, password: String): ResultHappen<FirebaseUser?>
    suspend fun getCurrentUserDetails(): ResultHappen<UserModel>
    suspend fun updateUserDetails(userModel: UserModel): ResultHappen<Unit>
    suspend fun getImageURLFromStorage(uri: Uri, context: Context): ResultHappen<String?>
    suspend fun getOnlineUser(): ResultHappen<List<UserModelFirestore>>
    suspend fun addFriendsAndSendNotification(currentUserID: String, email: String, pushNotification: PushNotification): ResultHappen<Unit>
}
