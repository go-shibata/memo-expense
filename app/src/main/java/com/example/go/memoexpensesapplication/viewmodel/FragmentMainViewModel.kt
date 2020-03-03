package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.component.MainComponent
import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import com.example.go.memoexpensesapplication.model.Expense
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

class FragmentMainViewModel : ViewModel() {

    @Inject
    lateinit var dispatcher: MainDispatcher

    private val _expenses = BehaviorProcessor.create<List<Expense>>()
    val expenses: Flowable<List<Expense>> = _expenses

    private val _addExpense = BehaviorProcessor.create<Expense>()
    val addExpense: Flowable<Expense> = _addExpense

    private val _deleteExpense = BehaviorProcessor.create<Expense>()
    val deleteExpense: Flowable<Expense> = _deleteExpense

    fun inject(mainComponent: MainComponent) {
        mainComponent.inject(this)
        subscribe()
    }

    private fun subscribe() {
        dispatcher.onGetAllExpenses
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_expenses)
        dispatcher.onAddExpense
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_addExpense)
        dispatcher.onDeleteExpense
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_deleteExpense)
    }
}