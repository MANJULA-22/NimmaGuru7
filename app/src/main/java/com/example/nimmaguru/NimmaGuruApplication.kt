package com.example.nimmaguru

import android.app.Application
import com.google.firebase.FirebaseApp

class NimmaGuruApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}