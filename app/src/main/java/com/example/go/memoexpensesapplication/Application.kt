package com.example.go.memoexpensesapplication

import android.app.Application

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        Prefs.initPrefs(this)
    }
}