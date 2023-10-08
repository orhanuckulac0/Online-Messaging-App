package com.myapp.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.data.model.UserModel
import com.myapp.data.model.UserModelFirestore
import com.myapp.domain.use_cases.GetOnlineUsersUseCase
import com.myapp.domain.use_cases.GetUserDetailsUseCase
import com.myapp.presentation.util.ResultHappen
import com.myapp.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val getOnlineUsersUseCase: GetOnlineUsersUseCase
): ViewModel() {

    private val _uiEvent =  MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var user: UserModel? = null

    private val _onlineUsers = MutableLiveData<List<UserModelFirestore>>()
    val onlineUsers: LiveData<List<UserModelFirestore>> = _onlineUsers

    init {
        getUserDetails()
        fetchOnlineUsers()
    }

    private fun getUserDetails(){
        viewModelScope.launch(Dispatchers.IO) {
            getUserDetailsUseCase.execute(
                onComplete = { userDetails->
                    user = userDetails
                    Log.i("MYTAG", "USER $user")
                },
                onError = { error->
                    Log.i("MYTAG", "ERROR $error")
                }
            )
        }
    }

    private fun fetchOnlineUsers() {
        viewModelScope.launch {
            val result = getOnlineUsersUseCase.execute()
            when (result) {
                is ResultHappen.Success -> {
                    val onlineUsers = result.data
                    _onlineUsers.value = onlineUsers
                }
                is ResultHappen.Error -> {
                    val errorMessage = result.message
                    // Handle the error
                }
            }
        }
    }


    fun onEvent(event: UiEvent){
        when(event) {
            is UiEvent.Navigate -> {
                sendUiEvent(UiEvent.Navigate(event.route))
            }
            is UiEvent.ShowToast -> {
                sendUiEvent(UiEvent.ShowToast(event.message))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiEvent.emit(event)
        }
    }

}