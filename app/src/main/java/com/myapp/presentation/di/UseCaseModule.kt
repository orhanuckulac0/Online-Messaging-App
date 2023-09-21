package com.myapp.presentation.di

import com.myapp.domain.RegisterUserUseCase
import com.myapp.presentation.firebase.FirebaseAuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideRegisterUseCase(firebaseAuthManager: FirebaseAuthManager): RegisterUserUseCase {
        return RegisterUserUseCase(firebaseAuthManager)
    }

}