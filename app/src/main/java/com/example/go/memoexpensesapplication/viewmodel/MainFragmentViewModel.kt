package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.model.Expense

class MainFragmentViewModel : ViewModel() {
    val data: MutableLiveData<ArrayList<Expense>> = MutableLiveData()

    init {
        data.value = ArrayList(emptyList())
    }
}