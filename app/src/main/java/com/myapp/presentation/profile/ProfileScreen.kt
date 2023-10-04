package com.myapp.presentation.profile

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.myapp.R
import com.myapp.data.model.UserModel
import com.myapp.presentation.util.StoreData
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    userDetails: UserModel?
) {
    var imageUri: Uri? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    val dataStore = remember { StoreData(context) }
    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
            it?.let { uri ->
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION )
                scope.launch {
                    dataStore.storeImage(uri.toString())
                    val user = userDetails!!.copy(
                        profileImage = uri.toString()
                    )
                    viewModel.updateUser(user)
                }
            }
        }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        if (imageUri != null){
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp),
                contentScale = ContentScale.Crop
            )
        }else{
            Image(painterResource(
                id = R.drawable.ic_user_place_holder),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        Button(
            onClick = {
                launcher.launch(arrayOf("image/*"))
            }
        ) {
            Text(text = "Pick Image")
        }
    }
    LaunchedEffect(key1 = true) {
        dataStore.getImage().collect {
            if (it != null) {
                imageUri = Uri.parse(it)
            }else{
                imageUri = null // Set imageUri to null or a default value
                Log.i("MYTAG", "NULL IMG")
            }
        }
    }
    Button(
        onClick = {
            onSignOut()
        }
    ) {
        Text(text = "Sign Out")
    }
}