package com.myapp.presentation.util

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

object Routes {

    const val LOG_IN = "log_in"
    const val REGISTER = "register"
    const val PROFILE = "profile"
    const val HOME = "home"

    fun NavOptionsBuilder.popUpToTop(navController: NavController) {
        popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
            inclusive =  true
        }
    }


}