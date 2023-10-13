package com.myapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var id: String? = null,
    var name: String,
    var surname: String,
    val email: String,
    var password: String? = null,
    var profileImage: String? = null,
    var loggedIn: Boolean,
    var fcmToken: String? = null

    ): Parcelable

// Separate data class for Firestore operations
data class UserModelFirestore(
    var id: String? = null,
    var name: String,
    var surname: String,
    val email: String,
    var password: String? = null,
    var profileImage: String? = null,
    var loggedIn: Boolean,
    var fcmToken: String? = null
) {
    constructor() : this("", "", "", "", null, null, false)
}
