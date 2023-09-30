package com.myapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.myapp.domain.repository.UserRepository
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
    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessagingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Profile Screen
                    var userProfileImageURL: Uri? by remember { mutableStateOf(null) }

                    LaunchedEffect(key1 = true) {
                        val userDetails = userRepository.getCurrentUserDetails()
                        userProfileImageURL = userDetails["profileImage"]?.toUri()
                    }

                    val updateProfileImage: (String) -> Unit = { newImageURL ->
                        userProfileImageURL = newImageURL.toUri()
                    }

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
                            ProfileScreen(
                                onProfileImageChanged = {
                                    updateProfileImage(it)
                                },
                                onSignOut = {
                                    firebaseAuth.signOut()
                                    navController.navigate(Routes.LOG_IN)
                                },
                                userProfileImageURL = userProfileImageURL?.toString() ?: ""
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