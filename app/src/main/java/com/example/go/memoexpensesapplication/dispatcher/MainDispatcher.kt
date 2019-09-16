package com.example.go.memoexpensesapplication.dispatcher

import com.example.go.memoexpensesapplication.action.MainAction
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.network.Database
import com.example.go.memoexpensesapplication.viewmodel.MainFragmentViewModel
import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

class MainDispatcher(private val viewModel: MainFragmentViewModel): Dispatcher<MainAction<Any?>> {
    private val _onChangeExpense: FlowableProcessor<ArrayList<Expense>> = PublishProcessor.create()
    val onChangeExpense: Flowable<ArrayList<Expense>> = _onChangeExpense

    override fun dispatch(action: MainAction<Any?>) {
        when (action) {
            is MainAction.GetExpense -> {
                Database.readExpenses {
                    val expenses = viewModel.data.value ?: arrayListOf()
                    expenses.apply {
                        if (isNotEmpty()) clear()
                        addAll(it)
                    }
                    _onChangeExpense.onNext(expenses)
                }
            }
            is MainAction.AddExpense -> {
                val expense = action.data
                Database.addExpense(expense) { id ->
                    expense.id = id
                    val expenses = viewModel.data.value ?: arrayListOf()
                    expenses.add(expense)
                    _onChangeExpense.onNext(expenses)
                }
            }
            is MainAction.DeleteExpense -> {
                val expense = action.data
                Database.deleteExpenses(expense) {
                    val expenses = viewModel.data.value ?: arrayListOf()
                    expenses.remove(expense)
                    _onChangeExpense.onNext(expenses)
                }
            }
        }
    }
}