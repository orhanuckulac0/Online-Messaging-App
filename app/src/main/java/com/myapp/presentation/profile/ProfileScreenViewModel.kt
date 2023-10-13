package com.myapp.presentation.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.data.model.UserModel
import com.myapp.domain.use_cases.GetUserDetailsUseCase
import com.myapp.domain.use_cases.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val updateUserUseCase: UpdateUserUseCase
): ViewModel() {

    private val _user: MutableLiveData<UserModel> = MutableLiveData()
    val user = _user

    init {
        getUserDetails()
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            getUserDetailsUseCase.execute(
                onComplete = {
                    _user.postValue(it)
                },
                onError = {
                    Log.i("MYTAG", "ERROR $it")
                }
            )
        }
    }

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