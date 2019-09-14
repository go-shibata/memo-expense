package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.view.adapter.RecyclerAdapter

class MainFragmentViewModel : ViewModel() {
    var data: ArrayList<Expense> = ArrayList(emptyList())
    var recyclerAdapter: RecyclerAdapter? = null
}