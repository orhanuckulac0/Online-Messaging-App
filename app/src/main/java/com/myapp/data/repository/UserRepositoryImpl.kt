package com.myapp.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.myapp.data.model.PushNotification
import com.myapp.data.model.UserModel
import com.myapp.data.model.UserModelFirestore
import com.myapp.data.repository.data_source.RemoteDataSource
import com.myapp.domain.repository.UserRepository
import com.myapp.presentation.util.ResultHappen
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    override suspend fun registerUser(userModel: UserModel): ResultHappen<FirebaseUser?> {
        return remoteDataSource.registerUser(userModel)
    }

    override suspend fun loginUser(email: String, password: String): ResultHappen<FirebaseUser?> {
        return remoteDataSource.loginUser(email, password)
    }

    override suspend fun getCurrentUserDetails(): ResultHappen<UserModel> {
        return remoteDataSource.getCurrentUserDetails()
    }

    override suspend fun updateUserDetails(userModel: UserModel): ResultHappen<Unit> {
        return remoteDataSource.updateUserDetails(userModel)
    }

    override suspend fun getImageURLFromStorage(uri: Uri, context: Context): ResultHappen<String?> {
        return remoteDataSource.getImageURLFromStorage(uri, context)
    }

    override suspend fun getOnlineUser(): ResultHappen<List<UserModelFirestore>> {
        return remoteDataSource.getOnlineUsers()
    }

    override suspend fun addFriendsAndSendNotification(
        currentUserID: String,
        email: String,
        pushNotification: PushNotification
    ): ResultHappen<Unit> {
        return remoteDataSource.addFriendsAndSendNotification(currentUserID, email, pushNotification)
    }
}
