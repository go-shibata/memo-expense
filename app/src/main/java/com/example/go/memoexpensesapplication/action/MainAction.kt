package com.example.go.memoexpensesapplication.action

import com.example.go.memoexpensesapplication.model.Expense

sealed class MainAction<out T>(val data: T) : Action {

    class GetExpense(data: String) : MainAction<String>(data)
    class AddExpense(data: Expense) : MainAction<Expense>(data)
    class DeleteExpense(data: Expense) : MainAction<Expense>(data)
}