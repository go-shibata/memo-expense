package com.example.go.memoexpensesapplication.actioncreator

import com.example.go.memoexpensesapplication.action.MainAction
import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.network.Database
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActionCreator @Inject constructor(
    private val database: Database,
    private val dispatcher: MainDispatcher
) {

    fun getAllExpenses(userUid: String) {
        database.readExpenses(userUid) {
            dispatcher.dispatch(MainAction.GetAllExpenses(it))
        }
    }

    fun addExpense(expense: Expense) {
        database.addExpense(expense) { id ->
            expense.id = id
            dispatcher.dispatch(MainAction.AddExpense(expense))
        }
    }

    fun editExpense(expense: Expense) {
        database.editExpense(expense) {
            dispatcher.dispatch(MainAction.EditExpense(expense))
        }
    }

    fun deleteExpense(expense: Expense) {
        database.deleteExpenses(expense) {
            dispatcher.dispatch(MainAction.DeleteExpense(expense))
        }
    }

    fun moveToTagList() {
        dispatcher.dispatch(MainAction.MoveToTagList())
    }
}