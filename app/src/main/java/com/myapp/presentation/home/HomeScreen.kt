package com.myapp.presentation.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.myapp.R
import com.myapp.data.model.NavigationItem
import com.myapp.presentation.navigation.Routes
import com.myapp.presentation.util.UiEvent
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    scrollBehavior: TopAppBarDefaults,
    context: Context
){
    val users = viewModel.onlineUsers.observeAsState()
    LaunchedEffect(key1 = users){
        Log.i("MYTAG", "${users.value}")
    }

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

    val items = listOf(
        NavigationItem(
            title = "Home",
            selectedIcon = R.drawable.home_clicked,
            unselectedIcon = R.drawable.home_unclicked,
            route = Routes.HOME
        ),
        NavigationItem(
            title = "Profile",
            selectedIcon = R.drawable.person_clicked,
            unselectedIcon = R.drawable.person_unclicked,
            route = Routes.PROFILE
        ),
        NavigationItem(
            title = "Friend Requests",
            selectedIcon = R.drawable.friend_requests_clicked,
            unselectedIcon = R.drawable.friend_requests_unclicked,
            route = Routes.PROFILE
        ),
        NavigationItem(
            title = "Settings",
            selectedIcon = R.drawable.settings_clicked,
            unselectedIcon = R.drawable.settings_unclicked,
            route = Routes.SETTINGS
        ),
    )

    AppBar(
        drawerState = drawerState,
        scope = scope,
        scrollBehavior = scrollBehavior.pinnedScrollBehavior(),
        items = items
        )
}