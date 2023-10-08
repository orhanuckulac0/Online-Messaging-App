package com.myapp.domain.use_cases

import android.util.Log
import com.myapp.data.model.UserModelFirestore
import com.myapp.domain.repository.UserRepository
import com.myapp.presentation.util.ResultHappen

class GetOnlineUsersUseCase(private val userRepository: UserRepository) {

    suspend fun execute(): ResultHappen<List<UserModelFirestore>> {
        return try {
            val result = userRepository.getOnlineUser()
            when (result) {
                is ResultHappen.Success -> {
                    Log.i("MYTAG", " data ${result.data}")
                    ResultHappen.Success(result.data)
                }
                is ResultHappen.Error -> {
                    ResultHappen.Error(result.message.toString())
                }
            }
        } catch (e: Exception) {
            ResultHappen.Error("An error occurred: ${e.message}")
        }
    }
}