package com.myapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: String? = null,
    var name: String,
    var surname: String,
    val email: String,
    var password: String? = null,
    var profileImage: String? = null
): Parcelable