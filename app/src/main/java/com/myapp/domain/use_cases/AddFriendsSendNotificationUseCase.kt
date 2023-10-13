package com.myapp.domain.use_cases

import android.util.Log
import com.myapp.data.model.PushNotification
import com.myapp.domain.repository.UserRepository
import com.myapp.presentation.util.ResultHappen

class AddFriendsSendNotificationUseCase(private val userRepository: UserRepository) {

    suspend fun execute(
        currentUserID: String,
        email: String,
        pushNotification: PushNotification
    ): ResultHappen<String> {
        return try {
            val result = userRepository.addFriendsAndSendNotification(currentUserID, email, pushNotification)
            when (result) {
                is ResultHappen.Success -> {
                    Log.i("MYTAG", "${result.data}")
                    ResultHappen.Success(result.data.toString())
                }
                is ResultHappen.Error -> {
                    Log.i("MYTAG", result.message.toString())
                    ResultHappen.Error(result.message.toString())
                }
            }
        } catch (e: Exception) {
            Log.i("MYTAG", e.message.toString())
            ResultHappen.Error("An error occurred: ${e.message}")
        }
    }
}
