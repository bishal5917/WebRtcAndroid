package com.example.webrtcandroid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.util.UUID

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        val user = UUID.randomUUID().toString().substring(0, 7)
    }
}