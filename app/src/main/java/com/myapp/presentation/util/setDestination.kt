package com.myapp.presentation.util

import com.google.firebase.auth.FirebaseAuth
import com.myapp.presentation.navigation.Routes

fun setDestination(firebaseAuth: FirebaseAuth): String {
    val user = firebaseAuth.currentUser
    return if (user != null){
        Routes.HOME
    }else{
        Routes.LOG_IN
    }
}