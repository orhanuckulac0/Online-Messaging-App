package com.myapp.domain.use_cases

import com.myapp.data.model.UserModel
import com.myapp.domain.repository.UserRepository
import com.myapp.presentation.util.ResultHappen
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun execute(
        onComplete: (UserModel) -> Unit,
        onError: (String) -> Unit
    ) {
        userRepository.getCurrentUserDetails().let {result->
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