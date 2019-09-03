package com.example.go.memoexpensesapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.fragment.MainFragment

class MainActivity :
    AppCompatActivity(),
    MainFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_container, MainFragment.newInstance())
            .commit()
    }

    override fun onFragmentInteraction() {
        println("NOT IMPLEMENT")
    }
}
