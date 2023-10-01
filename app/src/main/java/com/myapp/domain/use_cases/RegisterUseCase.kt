package com.myapp.domain.use_cases

import com.google.firebase.auth.FirebaseUser
import com.myapp.data.model.UserModel
import com.myapp.domain.repository.UserRepository
import com.myapp.presentation.util.ResultHappen
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun execute(userModel: UserModel, onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        userRepository.registerUser(userModel).let { result ->
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
