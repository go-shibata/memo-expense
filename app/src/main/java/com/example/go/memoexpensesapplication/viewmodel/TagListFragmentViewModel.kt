package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.Prefs
import com.example.go.memoexpensesapplication.action.Action
import com.example.go.memoexpensesapplication.action.TagListAction
import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import io.reactivex.disposables.Disposable

class TagListFragmentViewModel : ViewModel() {
    private val _data: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val data: LiveData<List<String>> = _data

    private val disposable: Disposable = MainDispatcher.subscribe(::reduce)

    private fun reduce(action: Action) {
        when (action) {
            is TagListAction.GetTag -> {
                val tags = Prefs.getTags().toList()
                _data.value = tags
            }
            is TagListAction.AddTag -> {
                TODO("Not Implemented")
            }
            is TagListAction.DeleteTag -> {
                TODO("Not Implemented")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun send(action: TagListAction<*>) {
        MainDispatcher.dispatch(action)
    }
}