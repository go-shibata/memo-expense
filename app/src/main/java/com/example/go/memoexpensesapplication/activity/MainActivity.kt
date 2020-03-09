package com.example.go.memoexpensesapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.databinding.ActivityMainBinding
import com.example.go.memoexpensesapplication.di.component.*
import com.example.go.memoexpensesapplication.fragment.LoginFragment
import com.example.go.memoexpensesapplication.fragment.MainFragment
import com.example.go.memoexpensesapplication.fragment.SplashFragment
import com.example.go.memoexpensesapplication.fragment.TagListFragment
import com.example.go.memoexpensesapplication.model.User
import com.example.go.memoexpensesapplication.navigator.FragmentLoginNavigator
import com.example.go.memoexpensesapplication.navigator.FragmentMainNavigator

class MainActivity :
    AppCompatActivity(),
    FragmentMainNavigator,
    FragmentLoginNavigator {

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

        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, SplashFragment.newInstance())
            .commit()
    }

    override fun onTransitionTagList() {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, TagListFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onLoggedIn(user: User) {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, MainFragment.newInstance(user))
            .commit()
    }

    override fun onAutoLoginFailed() {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, LoginFragment.newInstance())
            .commit()
    }
}
