package com.myapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.myapp.data.model.UserModel
import com.myapp.domain.use_cases.RegisterUserUseCase
import com.myapp.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
    ) : ViewModel() {

    private val _uiEvent =  MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

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

    suspend fun registerUser(
        userModel: UserModel,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        if (userModel.email.isEmpty()){
            sendUiEvent(UiEvent.ShowToast("Email can't be empty."))
        }else if(userModel.password!!.isEmpty()){
            sendUiEvent(UiEvent.ShowToast("Password can't be empty."))
        }else{
            registerUserUseCase.execute(userModel, onComplete, onError)
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiEvent.emit(event)
        }
    }

}
