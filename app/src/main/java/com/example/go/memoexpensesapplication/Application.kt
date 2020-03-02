package com.example.go.memoexpensesapplication

import android.app.Application
import android.content.Context

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Prefs.initPrefs(this)
        context = this
    }

    companion object {
        private lateinit var context: Context
        fun getContext(): Context = context
    }
}