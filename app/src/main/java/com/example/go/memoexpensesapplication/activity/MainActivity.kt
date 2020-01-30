package com.example.go.memoexpensesapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.fragment.ExpenseListFragment
import com.example.go.memoexpensesapplication.fragment.TagListFragment

class MainActivity :
    AppCompatActivity(),
    ExpenseListFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_container, ExpenseListFragment.newInstance())
            .commit()
    }

    override fun onTransitionTagList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_container, TagListFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}
