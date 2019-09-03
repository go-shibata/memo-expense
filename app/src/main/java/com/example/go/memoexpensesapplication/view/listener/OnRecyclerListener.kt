package com.example.go.memoexpensesapplication.view.listener

import android.view.View
import com.example.go.memoexpensesapplication.model.Expense

interface OnRecyclerListener {
    fun onRecyclerClicked(v: View, position: Int, item: Expense)
}