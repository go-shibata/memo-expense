package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.component.LoginComponent
import com.example.go.memoexpensesapplication.dispatcher.LoginDispatcher
import com.example.go.memoexpensesapplication.model.User
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

class FragmentLoginViewModel : ViewModel() {

    @Inject
    lateinit var dispatcher: LoginDispatcher

    val mail: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    private val _login = BehaviorProcessor.create<User>()
    private val _authenticationFail = BehaviorProcessor.create<Unit>()
    val login: Flowable<User> = _login
    val authenticationFail: Flowable<Unit> = _authenticationFail

    fun inject(loginComponent: LoginComponent) {
        loginComponent.inject(this)
        subscribe()
    }

    private fun subscribe() {
        dispatcher.onLogin
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_login)
        dispatcher.onAuthenticationFail
            .map { action ->
                println(action)
                action.data
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_authenticationFail)
    }
}