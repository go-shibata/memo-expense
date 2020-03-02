package com.example.go.memoexpensesapplication.navigator

import com.example.go.memoexpensesapplication.model.User

interface FragmentLoginNavigator {
    fun onLoggedIn(user: User)
}