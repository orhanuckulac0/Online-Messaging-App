package com.myapp.presentation.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.myapp.data.repository.UserRepositoryImpl
import com.myapp.data.repository.data_source.RemoteDataSource
import com.myapp.data.repository.datasource_impl.RemoteDataSourceImpl
import com.myapp.data.repository.fcm.RetrofitInstance
import com.myapp.domain.repository.UserRepository
import com.myapp.presentation.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Modules {

    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: RemoteDataSource): UserRepository {
        return UserRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        firebaseMessaging: FirebaseMessaging,
        retrofitInstance: RetrofitInstance
    ): RemoteDataSource {
        return RemoteDataSourceImpl(
            firebaseAuth,
            firestore,
            firebaseMessaging,
            retrofitInstance
        )
    }
}
