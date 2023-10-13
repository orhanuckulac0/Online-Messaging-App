package com.myapp.presentation.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myapp.data.model.NotificationData
import com.myapp.data.model.PushNotification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendsDialog(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit
    ){
    var targetEmail by remember { mutableStateOf("") }

    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(
                dismissOnClickOutside = true
            )
        ) {
            Surface(
                modifier = Modifier.size(400.dp, 250.dp),
                shape = MaterialTheme.shapes.medium,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Enter an email address")
                    Spacer(modifier = Modifier.padding(5.dp))
                    TextField(
                        value = targetEmail,
                        onValueChange = { newText ->
                            targetEmail = newText
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onDismiss()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onDismiss()
                            PushNotification(
                                NotificationData("title", "message"),
                                viewModel.user?.fcmToken!!
                            ).also {
                                viewModel.addFriends(viewModel.user?.id!!, targetEmail, it)
                            }
                        },
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}
