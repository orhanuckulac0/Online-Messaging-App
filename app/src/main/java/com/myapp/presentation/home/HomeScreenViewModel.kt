package com.myapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.data.model.UserModel
import com.myapp.domain.use_cases.GetUserDetailsUseCase
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
): ViewModel() {

    private val _uiEvent =  MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var user: UserModel? = null

    init {
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