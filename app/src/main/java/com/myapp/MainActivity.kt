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
import com.myapp.data.model.UserModel
import com.myapp.presentation.home.HomeScreen
import com.myapp.presentation.profile.ProfileScreen
import com.myapp.presentation.register.RegisterScreen
import com.myapp.presentation.sign_in.LoginScreen
import com.myapp.presentation.util.Routes
import com.myapp.presentation.util.setDestination
import com.myapp.ui.theme.MessagingAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessagingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // navigation
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = setDestination(firebaseAuth)
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
                                    firebaseAuth.signOut()
                                    navController.navigate(Routes.LOG_IN)
                                }
                            )
                        }

                        composable(route = Routes.REGISTER){
                            RegisterScreen(
                                navController = navController
                            )
                        }

                        composable(route = Routes.HOME){
                            HomeScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}