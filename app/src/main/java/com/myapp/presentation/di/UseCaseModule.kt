package com.myapp.presentation.di

import com.myapp.domain.use_cases.LoginUseCase
import com.myapp.domain.use_cases.RegisterUserUseCase
import com.myapp.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideRegisterUseCase(firebaseAuthManager: UserRepository): RegisterUserUseCase {
        return RegisterUserUseCase(firebaseAuthManager)
    }

    @Provides
    fun provideLoginUseCase(firebaseAuthManager: UserRepository): LoginUseCase {
        return LoginUseCase(firebaseAuthManager)
    }

}