package com.myapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.myapp.domain.RegisterUserUseCase
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

    fun registerUser(
        email: String,
        password: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isEmpty()){
            sendUiEvent(UiEvent.ShowToast("Email can't be empty."))
        }else if(password.isEmpty()){
            sendUiEvent(UiEvent.ShowToast("Password can't be empty."))
        }else{
            registerUserUseCase.execute(email, password, onComplete, onError)
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiEvent.emit(event)
        }
    }

}
