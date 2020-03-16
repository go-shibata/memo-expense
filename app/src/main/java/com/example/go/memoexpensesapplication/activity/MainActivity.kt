package com.example.go.memoexpensesapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.databinding.ActivityMainBinding
import com.example.go.memoexpensesapplication.di.component.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var loginComponent: LoginComponent
    lateinit var mainComponent: MainComponent
    lateinit var tagListComponent: TagListComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        loginComponent = DaggerLoginComponent.create()
        mainComponent = DaggerMainComponent.create()
        tagListComponent = DaggerTagListComponent.create()
    }
}
