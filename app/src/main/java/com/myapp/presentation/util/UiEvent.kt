package com.myapp.presentation.util

sealed class UiEvent {

    data class Navigate(val route: String): UiEvent()
    data class ShowToast(val message: String): UiEvent()

}