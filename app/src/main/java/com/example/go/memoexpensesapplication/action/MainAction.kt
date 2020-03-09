package com.example.go.memoexpensesapplication.action

import com.example.go.memoexpensesapplication.model.Expense

sealed class MainAction<out T>(val data: T) : Action {

    class GetAllExpenses(expenses: List<Expense>) : MainAction<List<Expense>>(expenses)
    class AddExpense(expense: Expense) : MainAction<Expense>(expense)
    class EditExpense(expense: Expense) : MainAction<Expense>(expense)
    class DeleteExpense(expense: Expense) : MainAction<Expense>(expense)
    class MoveToTagList : MainAction<Unit>(Unit)
}