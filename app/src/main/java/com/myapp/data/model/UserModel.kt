package com.myapp.data.model

data class UserModel(
    val id: String? = null,
    val name: String,
    val surname: String,
    val email: String,
    val password: String? = null,
    val profileImage: String? = null
)