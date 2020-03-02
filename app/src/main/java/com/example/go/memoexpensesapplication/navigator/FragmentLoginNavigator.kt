package com.example.go.memoexpensesapplication.navigator

import com.example.go.memoexpensesapplication.model.User

interface ListFragmentNavigator {
    fun onLoggedIn(user: User)
}