package com.example.go.memoexpensesapplication.action

import com.example.go.memoexpensesapplication.model.Expense

sealed class MainAction<out T>(val data: T) : Action {

    class GetExpense : MainAction<Unit>(Unit)
    class AddExpense(data: Expense) : MainAction<Expense>(data)
    class DeleteExpense(data: Expense) : MainAction<Expense>(data)
}