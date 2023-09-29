package com.myapp.presentation.register

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.myapp.presentation.util.Routes
import com.myapp.presentation.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterScreenViewModel = hiltViewModel(),
    navController: NavController
    ){

    BackHandler {
        navController.popBackStack()
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{event->
            when(event){
                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val email = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
        val name = remember { mutableStateOf(TextFieldValue()) }
        val surname = remember { mutableStateOf(TextFieldValue()) }

        Text(
            text = "Register",
            style = TextStyle(fontSize = 40.sp)
        )

        Spacer(modifier = Modifier.height(20.dp))


        TextField(
            label = { Text(text = "Name") },
            value = name.value,
            onValueChange = { name.value = it })

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Surname") },
            value = surname.value,
            onValueChange = { surname.value = it })

        Spacer(modifier = Modifier.height(20.dp))


        TextField(
            label = { Text(text = "Email") },
            value = email.value,
            onValueChange = { email.value = it })

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Password") },
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it })

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    viewModel.registerUser(
                        email = email.value.text,
                        password = password.value.text,
                        name = name.value.text,
                        surname = surname.value.text,
                        profileImage = "",
                        onComplete = { viewModel.onEvent(UiEvent.Navigate(Routes.LOG_IN))},
                        onError = { errorMessage->
                            viewModel.onEvent(UiEvent.ShowToast(errorMessage))
                        }
                    )
                          },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Register")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clickable { viewModel.onEvent(UiEvent.Navigate(Routes.LOG_IN)) }
        ) {
            Text(
                text = "Already have an account?",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = AnnotatedString("Login"),
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}