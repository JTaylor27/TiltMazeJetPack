package com.example.tiltmaze

import android.app.Application
import android.content.Context

class TiltMazeApplication: Application() {
    lateinit var appContext: Context

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
    }
}