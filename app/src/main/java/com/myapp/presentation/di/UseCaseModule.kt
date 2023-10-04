package com.myapp.presentation.di

import com.myapp.domain.use_cases.LoginUseCase
import com.myapp.domain.use_cases.RegisterUserUseCase
import com.myapp.domain.repository.UserRepository
import com.myapp.domain.use_cases.GetImageUrlUseCase
import com.myapp.domain.use_cases.GetUserDetailsUseCase
import com.myapp.domain.use_cases.UpdateUserUseCase
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

    @Provides
    fun provideUpdateUserUseCase(firebaseAuthManager: UserRepository): UpdateUserUseCase {
        return UpdateUserUseCase(firebaseAuthManager)
    }

    @Provides
    fun provideGetUseDetailsUseCase(firebaseAuthManager: UserRepository): GetUserDetailsUseCase {
        return GetUserDetailsUseCase(firebaseAuthManager)
    }

    @Provides
    fun provideGetImageUrlUseCase(firebaseAuthManager: UserRepository): GetImageUrlUseCase {
        return GetImageUrlUseCase(firebaseAuthManager)
    }
}