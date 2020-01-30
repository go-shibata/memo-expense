package com.example.go.memoexpensesapplication.dispatcher

import com.example.go.memoexpensesapplication.action.Action

interface Dispatcher<T: Action> {
    fun dispatch(action: T)
}