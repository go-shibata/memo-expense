package com.example.go.memoexpensesapplication.action

import com.example.go.memoexpensesapplication.model.Expense

sealed class MainAction<out T> : Action<T> {
    class GetExpense : MainAction<Unit>() {
        override val data: Unit = Unit
    }
    class AddExpense(override val data: Expense) : MainAction<Expense>()
    class DeleteExpense(override val data: Expense) : MainAction<Expense>()
}