package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.model.Expense

class MainFragmentViewModel : ViewModel() {
    var data: ArrayList<Expense> = ArrayList(emptyList())
}