package com.myapp.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.myapp.data.model.UserModel
import com.myapp.domain.use_cases.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase
): ViewModel() {

    suspend fun updateUser(userModel: UserModel){
        updateUserUseCase.execute(
            userModel = userModel,
            onComplete = {
                Log.i("MYTAG", "SUCCESSFULLY UPDATED USER")
            },
            onError = {
                Log.i("MYTAG", "USER UPDATE FAILED $it")
            }
        )
    }
}