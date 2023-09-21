package com.myapp.presentation.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.myapp.domain.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
    ) : ViewModel() {

    fun registerUser(email: String, password: String) {
        registerUserUseCase.execute(email, password,
            { user ->
                Log.i("MYTAG","VIEW MODEL REGISTERED ${user!!.email}")
            },
            { errorMessage ->
                Log.i("MYTAG","VIEW MODEL, $errorMessage")
                Log.i("MYTAG","VIEW MODEL, $email $password")
            }
        )
    }

}
