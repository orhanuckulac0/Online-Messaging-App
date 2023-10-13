package com.myapp.data.repository.data_source

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.myapp.data.model.PushNotification
import com.myapp.data.model.UserModel
import com.myapp.data.model.UserModelFirestore
import com.myapp.presentation.util.ResultHappen

interface RemoteDataSource {
    suspend fun getCurrentUserDetails(): ResultHappen<UserModel>
    suspend fun registerUser(userModel: UserModel): ResultHappen<FirebaseUser?>
    suspend fun loginUser(email: String, password: String): ResultHappen<FirebaseUser?>
    suspend fun updateUserDetails(userModel: UserModel): ResultHappen<Unit>

    suspend fun getImageURLFromStorage(uri: Uri, context: Context): ResultHappen<String?>
    suspend fun getOnlineUsers(): ResultHappen<List<UserModelFirestore>>
    suspend fun addFriendsAndSendNotification(currentUserID: String, email: String, pushNotification: PushNotification): ResultHappen<Unit>
}
