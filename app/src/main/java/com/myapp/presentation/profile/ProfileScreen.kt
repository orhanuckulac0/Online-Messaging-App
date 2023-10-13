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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.myapp.R
import com.myapp.presentation.util.StoreData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit,
    ) {

    val user by viewModel.user.observeAsState(initial = null)

    var imageUri: Uri? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    val dataStore = remember { StoreData(context) }
    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
            it?.let { uri ->
                context.contentResolver
                    .takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                scope.launch {
                    dataStore.storeImage(uri.toString())
                    val userToUpdate = user!!.copy(
                        profileImage = uri.toString()
                    )
                    viewModel.updateUser(userToUpdate)
                }
            }
        }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Profile")
                    },
                navigationIcon = {
                    IconButton(onClick = {
                        onPopBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = Color.Black,
                            contentDescription = "Navigate Back",
                        )
                    }
                }
            ) },
        content = {
            it.calculateTopPadding()
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {
                
                Text(text = "Hello ${user?.name}")
                
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
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.updateUser(user!!.copy(
                                loggedIn = false
                            ))
                            onSignOut()
                        }
                    }
                ) {
                    Text(text = "Sign Out")
                }
            }
            LaunchedEffect(key1 = true) {
                dataStore.getImage().collect { image->
                    if (image != null) {
                        imageUri = Uri.parse(image)
                    }else{
                        imageUri = null // Set imageUri to null or a default value
                        Log.i("MYTAG", "NULL IMG")
                    }
                }
            }

        }
    )
}