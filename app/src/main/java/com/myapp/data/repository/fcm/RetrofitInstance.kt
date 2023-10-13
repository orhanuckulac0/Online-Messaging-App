package com.myapp.data.repository.fcm

import retrofit2.Retrofit
import javax.inject.Inject

class RetrofitInstance @Inject constructor(private val retrofit: Retrofit) {

    val api: NotificationAPI by lazy {
        retrofit.create(NotificationAPI::class.java)
    }
}
