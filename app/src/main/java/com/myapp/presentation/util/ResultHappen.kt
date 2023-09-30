package com.myapp.presentation.util

sealed class ResultHappen<out T> {
    data class Success<out T>(val data: T) : ResultHappen<T>()
    data class Error(val message: String?) : ResultHappen<Nothing>()
}
