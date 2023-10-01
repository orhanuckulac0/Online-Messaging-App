package com.myapp.presentation.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.myapp.R
import com.myapp.data.model.UserModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onProfileImageChanged: (String) -> Unit,
    onSignOut: () -> Unit,
    userProfileImageURL: String,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        onProfileImageChanged(uri.toString())
        val user = UserModel(
            null,
            "test",
            "test surname",
            "test@gmail.com",
            null,
            uri.toString()
        )
        scope.launch {
            viewModel.updateUser(userModel = user)
        }
    }

    val painter =
        rememberAsyncImagePainter(ImageRequest.Builder
            (context)
            .data(data = userProfileImageURL)
            .apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                placeholder(R.drawable.ic_user_place_holder)
                error(R.drawable.ic_user_place_holder)
            }).build()
        )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(4.dp)
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        Button(onClick = {
            imagePicker.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )

        }) {
            Text(text = "Change Profile Picture")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onSignOut) {
            Text(text = "Sign out")
        }
    }
}