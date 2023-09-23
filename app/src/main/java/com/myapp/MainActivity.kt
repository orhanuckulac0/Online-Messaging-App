package com.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.myapp.presentation.home.HomeScreen
import com.myapp.presentation.profile.ProfileScreen
import com.myapp.presentation.register.RegisterScreen
import com.myapp.presentation.sign_in.LoginScreen
import com.myapp.presentation.util.Routes
import com.myapp.ui.theme.MessagingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessagingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.LOG_IN) {
                        composable(Routes.LOG_IN) {
                            LoginScreen(
                                navController = navController
                            )
                        }
                        composable(route = Routes.PROFILE){
                            ProfileScreen(
                                onSignOut = {
                                    firebaseAuth.signOut()
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(route = Routes.REGISTER){
                            RegisterScreen(navController = navController)
                        }
                        composable(route = Routes.HOME){
                            HomeScreen()
                        }
                    }
                }
            }
        }
    }
}