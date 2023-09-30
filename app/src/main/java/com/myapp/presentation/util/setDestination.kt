package com.myapp.presentation.util

import com.google.firebase.auth.FirebaseAuth

fun setDestination(firebaseAuth: FirebaseAuth): String {
    val user = firebaseAuth.currentUser
    return if (user != null){
        Routes.HOME
    }else{
        Routes.LOG_IN
    }
}