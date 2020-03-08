package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.dispatcher.LoginDispatcher
import com.example.go.memoexpensesapplication.navigator.FragmentLoginNavigator
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class FragmentLoginViewModel @Inject constructor(
    dispatcher: LoginDispatcher
) : ViewModel() {

    private var navigator: FragmentLoginNavigator? = null

    val mail: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    private val login: Disposable
    private val _authenticationFail = PublishProcessor.create<Unit>()
    val authenticationFail: Flowable<Unit> = _authenticationFail
    private val autoLoginFail: Disposable

    init {
        login = dispatcher.onLogin
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    navigator?.onLoggedIn(user) ?: throw RuntimeException("Navigator must be set")
                },
                { throw it }
            )
        dispatcher.onAuthenticationFail
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_authenticationFail)
        autoLoginFail = dispatcher.onAutoLoginFail
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    navigator?.onAutoLoginFailed()
                        ?: throw RuntimeException("Navigator must be set")
                },
                { throw it }
            )
    }

    fun setNavigator(navigator: FragmentLoginNavigator) {
        this.navigator = navigator
    }

    override fun onCleared() {
        login.dispose()
        autoLoginFail.dispose()
        super.onCleared()
    }
}