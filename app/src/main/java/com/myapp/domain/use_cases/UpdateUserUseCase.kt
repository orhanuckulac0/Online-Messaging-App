package com.myapp.domain.use_cases

import com.myapp.data.model.UserModel
import com.myapp.domain.repository.UserRepository
import com.myapp.presentation.util.ResultHappen

class UpdateUserUseCase(private val userRepository: UserRepository) {

    suspend fun execute(
        userModel: UserModel,
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ) {
        userRepository.updateUserDetails(userModel).let { result->
            when (result) {
                is ResultHappen.Success -> {
                    onComplete()
                }
                is ResultHappen.Error -> {
                    onError(result.message.toString())
                }
            }
        }
    }
}