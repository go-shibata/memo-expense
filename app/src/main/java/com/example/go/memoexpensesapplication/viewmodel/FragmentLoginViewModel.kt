package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.component.LoginComponent
import com.example.go.memoexpensesapplication.dispatcher.LoginDispatcher
import com.example.go.memoexpensesapplication.navigator.FragmentLoginNavigator
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

class FragmentLoginViewModel : ViewModel() {

    private lateinit var navigator: FragmentLoginNavigator

    @Inject
    lateinit var dispatcher: LoginDispatcher

    val mail: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    private lateinit var login: Disposable
    private val _authenticationFail = BehaviorProcessor.create<Unit>()
    val authenticationFail: Flowable<Unit> = _authenticationFail

    fun setNavigator(navigator: FragmentLoginNavigator) {
        this.navigator = navigator
    }

    fun inject(loginComponent: LoginComponent) {
        loginComponent.inject(this)
        subscribe()
    }

    private fun subscribe() {
        login = dispatcher.onLogin
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { user -> navigator.onLoggedIn(user) }
        dispatcher.onAuthenticationFail
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_authenticationFail)
    }

    override fun onCleared() {
        login.dispose()
        super.onCleared()
    }
}