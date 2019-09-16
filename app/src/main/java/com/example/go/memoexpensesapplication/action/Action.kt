package com.example.go.memoexpensesapplication.action

interface Action<out T> {
    val data: T
}