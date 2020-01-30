package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.action.Action
import com.example.go.memoexpensesapplication.action.MainAction
import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.network.Database
import io.reactivex.disposables.Disposable

class MainFragmentViewModel : ViewModel() {
    private val _data: MutableLiveData<List<Expense>> = MutableLiveData(emptyList())
    val data: LiveData<List<Expense>> = _data

    private val disposable: Disposable = MainDispatcher.subscribe(::reduce)

    private fun reduce(action: Action) {
        when (action) {
            is MainAction.GetExpense -> {
                Database.readExpenses {
                    _data.value = it
                }
            }
            is MainAction.AddExpense -> {
                val expense = action.data
                Database.addExpense(expense) { id ->
                    expense.id = id
                    _data.value = _data.value.orEmpty() + expense
                }
            }
            is MainAction.DeleteExpense -> {
                val expense = action.data
                Database.deleteExpenses(expense) {
                    _data.value = _data.value.orEmpty() - expense
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun send(action: MainAction<*>) {
        MainDispatcher.dispatch(action)
    }
}