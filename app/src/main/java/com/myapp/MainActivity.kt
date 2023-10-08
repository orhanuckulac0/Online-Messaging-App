package com.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.myapp.presentation.navigation.NavGraph
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
                if (firebaseAuth.currentUser == null){
                    firebaseAuth.signOut()
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        startingDestination = setDestination(firebaseAuth),
                        onSignOut = { firebaseAuth.signOut() }
                    )
                }
            }
        }
    }
}