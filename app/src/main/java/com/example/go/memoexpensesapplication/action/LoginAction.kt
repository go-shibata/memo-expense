package com.example.go.memoexpensesapplication.action

import com.example.go.memoexpensesapplication.model.User

sealed class LoginAction<out T>(val data: T) : Action {

    class Login(user: User) : LoginAction<User>(user)
    class CreateUserFail : LoginAction<Unit>(Unit)
    class AuthenticationFail : LoginAction<Unit>(Unit)
    class AutoLoginFail : LoginAction<Unit>(Unit)
}