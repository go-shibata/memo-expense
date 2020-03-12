package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.navigator.FragmentMainNavigator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class FragmentMainViewModel @Inject constructor(
    dispatcher: MainDispatcher
) : ViewModel() {

    private var navigator: FragmentMainNavigator? = null

    val isCheckable: MutableLiveData<Boolean> = MutableLiveData(false)
    val expenses: MutableLiveData<List<Expense>> = MutableLiveData(emptyList())

    private val compositeDisposable = CompositeDisposable()

    init {
        dispatcher.onGetAllExpenses
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { expenses.postValue(it) }
            .addTo(compositeDisposable)
        dispatcher.onAddExpense
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val list = (expenses.value ?: emptyList()) + it
                expenses.postValue(list)
            }.addTo(compositeDisposable)
        dispatcher.onEditExpense
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { expense ->
                var list = expenses.value ?: emptyList()
                list = list - list.single { it.id == expense.id }
                list = list + expense
                expenses.postValue(list)
            }.addTo(compositeDisposable)
        dispatcher.onDeleteExpense
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val list = (expenses.value ?: emptyList()) - it
                expenses.postValue(list)
            }.addTo(compositeDisposable)
        dispatcher.onMoveToTagList
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    navigator?.onTransitionTagList()
                        ?: throw RuntimeException("Navigator must be set")
                },
                { throw it }
            ).addTo(compositeDisposable)
        dispatcher.onToggleCheckable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isCheckable.postValue(isCheckable.value?.not())
            }.addTo(compositeDisposable)
    }

    fun setNavigator(navigator: FragmentMainNavigator) {
        this.navigator = navigator
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}