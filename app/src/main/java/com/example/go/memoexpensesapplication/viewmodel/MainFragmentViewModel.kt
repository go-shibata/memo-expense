package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.action.MainAction
import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import com.example.go.memoexpensesapplication.model.Expense
import io.reactivex.android.schedulers.AndroidSchedulers

class MainFragmentViewModel : ViewModel() {
    private val dispatcher = MainDispatcher(this)

    val data: LiveData<ArrayList<Expense>> = dispatcher.onChangeExpense
        .observeOn(AndroidSchedulers.mainThread())
        .to {
            LiveDataReactiveStreams.fromPublisher(it)
        }

    fun send(action: MainAction<*>) {
        dispatcher.dispatch(action)
    }
}