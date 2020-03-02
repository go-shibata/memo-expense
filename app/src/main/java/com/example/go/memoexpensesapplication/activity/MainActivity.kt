package com.example.go.memoexpensesapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.databinding.ActivityMainBinding
import com.example.go.memoexpensesapplication.fragment.ExpenseListFragment
import com.example.go.memoexpensesapplication.fragment.LoginFragment
import com.example.go.memoexpensesapplication.fragment.TagListFragment
import com.example.go.memoexpensesapplication.model.User
import com.example.go.memoexpensesapplication.navigator.ListFragmentNavigator
import com.example.go.memoexpensesapplication.viewmodel.FragmentLoginViewModel

class MainActivity :
    AppCompatActivity(),
    ExpenseListFragment.OnFragmentInteractionListener,
    ListFragmentNavigator {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        val fragmentLoginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[FragmentLoginViewModel::class.java]
        fragmentLoginViewModel.setNavigator(this)
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, LoginFragment.newInstance(fragmentLoginViewModel))
            .commit()
    }

    override fun onTransitionTagList() {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, TagListFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onLoggedIn(user: User) {
        // TODO: ExpenseListFragment に　User を渡す
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, ExpenseListFragment.newInstance())
            .commit()
    }
}
