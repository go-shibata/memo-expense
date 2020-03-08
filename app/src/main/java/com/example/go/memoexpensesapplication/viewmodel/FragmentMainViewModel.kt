package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.di.component.MainComponent
import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.navigator.FragmentMainNavigator
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class FragmentMainViewModel : ViewModel() {

    private lateinit var navigator: FragmentMainNavigator

    @Inject
    lateinit var dispatcher: MainDispatcher

    private val _expenses = PublishProcessor.create<List<Expense>>()
    val expenses: Flowable<List<Expense>> = _expenses
    private val _addExpense = PublishProcessor.create<Expense>()
    val addExpense: Flowable<Expense> = _addExpense
    private val _deleteExpense = PublishProcessor.create<Expense>()
    val deleteExpense: Flowable<Expense> = _deleteExpense
    private lateinit var moveToTagList: Disposable

    fun setNavigator(navigator: FragmentMainNavigator) {
        this.navigator = navigator
    }

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
        moveToTagList = dispatcher.onMoveToTagList
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { navigator.onTransitionTagList() }
    }

    override fun onCleared() {
        moveToTagList.dispose()
        super.onCleared()
    }
}