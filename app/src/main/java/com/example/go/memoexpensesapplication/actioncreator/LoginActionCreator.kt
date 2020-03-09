package com.example.go.memoexpensesapplication.actioncreator

import com.example.go.memoexpensesapplication.action.LoginAction
import com.example.go.memoexpensesapplication.dispatcher.LoginDispatcher
import com.example.go.memoexpensesapplication.model.User
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginActionCreator @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val dispatcher: LoginDispatcher
) {

    fun checkLogin() {
        val currentUser = mAuth.currentUser
        currentUser?.let { user ->
            val mUser = User(user)
            dispatcher.dispatch(LoginAction.Login(mUser))
        } ?: dispatcher.dispatch(LoginAction.AutoLoginFail())
    }

    fun createUser(mail: String, password: String) {
        mAuth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.let { user ->
                        val mUser = User(user)
                        dispatcher.dispatch(LoginAction.Login(mUser))
                        return@addOnCompleteListener
                    }
                }

                dispatcher.dispatch(LoginAction.CreateUserFail())
            }
    }

    fun login(mail: String, password: String) {
        mAuth.signInWithEmailAndPassword(mail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.let { user ->
                        val mUser = User(user)
                        dispatcher.dispatch(LoginAction.Login(mUser))
                        return@addOnCompleteListener
                    }
                }

                dispatcher.dispatch(LoginAction.AuthenticationFail())
            }
    }
}