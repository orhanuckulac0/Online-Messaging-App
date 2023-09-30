package com.myapp.data.repository

import com.google.firebase.auth.FirebaseUser
import com.myapp.data.repository.data_source.RemoteDataSource
import com.myapp.domain.repository.UserRepository
import com.myapp.presentation.util.ResultHappen
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    override suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        surname: String,
        profileImage: String
    ): ResultHappen<FirebaseUser?> {
        return remoteDataSource.registerUser(email, password, name, surname, profileImage)
    }

    override suspend fun loginUser(email: String, password: String): ResultHappen<FirebaseUser?> {
        return remoteDataSource.loginUser(email, password)
    }

    override suspend fun getCurrentUserDetails(): HashMap<String, String?> {
        return remoteDataSource.getCurrentUserDetails()
    }

    override suspend fun updateUserDetails(
        name: String,
        surname: String,
        email: String,
        profileImage: String
    ): ResultHappen<Unit> {
        return remoteDataSource.updateUserDetails(name, surname, email, profileImage)
    }
}
