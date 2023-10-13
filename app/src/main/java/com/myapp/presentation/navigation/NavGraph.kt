package com.myapp.presentation.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.myapp.data.model.UserModel
import com.myapp.presentation.home.HomeScreen
import com.myapp.presentation.profile.ProfileScreen
import com.myapp.presentation.register.RegisterScreen
import com.myapp.presentation.sign_in.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    startingDestination: String,
    onSignOut: () -> Unit
){

    NavHost(
        navController = navController,
        startDestination = startingDestination
    ) {

        composable(Routes.LOG_IN) {
            LoginScreen(
                navController = navController
            )
        }

        composable(route = Routes.PROFILE){
            val user = navController.previousBackStackEntry?.savedStateHandle?.get<UserModel>("user")
            ProfileScreen(
                userDetails = user,
                onSignOut = {
                    navController.navigate(Routes.LOG_IN)
                    onSignOut()
                },
                onPopBackStack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.REGISTER){
            RegisterScreen(
                navController = navController
            )
        }

        composable(route = Routes.HOME){
            // home screen hoisting
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val scrollBehavior = TopAppBarDefaults
            val context = LocalContext.current

            HomeScreen(
                navController = navController,
                drawerState = drawerState,
                scope = scope,
                scrollBehavior = scrollBehavior,
                context = context
            )
        }
    }
}