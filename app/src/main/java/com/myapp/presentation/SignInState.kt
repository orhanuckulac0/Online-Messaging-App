package com.myapp.presentation

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInErrorMessage: String? = null
)
