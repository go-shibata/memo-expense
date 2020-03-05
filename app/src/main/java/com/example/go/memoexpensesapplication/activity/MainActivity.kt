package com.example.go.memoexpensesapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.databinding.ActivityMainBinding
import com.example.go.memoexpensesapplication.fragment.LoginFragment
import com.example.go.memoexpensesapplication.fragment.MainFragment
import com.example.go.memoexpensesapplication.fragment.TagListFragment
import com.example.go.memoexpensesapplication.model.User
import com.example.go.memoexpensesapplication.navigator.FragmentLoginNavigator
import com.example.go.memoexpensesapplication.navigator.FragmentMainNavigator

class MainActivity :
    AppCompatActivity(),
    FragmentMainNavigator,
    FragmentLoginNavigator {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, LoginFragment.newInstance())
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
            .replace(binding.container.id, MainFragment.newInstance(user, this))
            .commit()
    }
}
