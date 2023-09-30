package com.myapp.data.model

import androidx.compose.ui.graphics.vector.ImageVector

class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)