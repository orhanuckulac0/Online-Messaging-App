package com.myapp.domain.use_cases

import com.google.firebase.auth.FirebaseUser
import com.myapp.domain.repository.UserRepository
import com.myapp.presentation.util.ResultHappen
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun execute(
        email: String,
        password: String,
        name: String,
        surname: String,
        profileImage: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        userRepository.registerUser(
            email = email,
            password = password,
            name = name,
            surname = surname,
            profileImage = profileImage
        ).let { result ->
            when (result) {
                is ResultHappen.Success -> {
                    onComplete(result.data)
                }
                is ResultHappen.Error -> {
                    onError(result.message.toString())
                }
            }
        }
    }
}
