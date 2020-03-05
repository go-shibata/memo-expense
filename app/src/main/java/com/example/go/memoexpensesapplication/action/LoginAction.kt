package com.example.go.memoexpensesapplication.action

import com.example.go.memoexpensesapplication.model.User

sealed class LoginAction<out T>(val data: T) : Action {

    class Login(user: User) : LoginAction<User>(user)
    class AuthenticationFail : LoginAction<Unit>(Unit)
}