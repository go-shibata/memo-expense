package com.example.go.memoexpensesapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.databinding.ActivityMainBinding
import com.example.go.memoexpensesapplication.fragment.ExpenseListFragment
import com.example.go.memoexpensesapplication.fragment.TagListFragment

class MainActivity :
    AppCompatActivity(),
    ExpenseListFragment.OnFragmentInteractionListener {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, ExpenseListFragment.newInstance())
            .commit()
    }

    override fun onTransitionTagList() {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, TagListFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}
