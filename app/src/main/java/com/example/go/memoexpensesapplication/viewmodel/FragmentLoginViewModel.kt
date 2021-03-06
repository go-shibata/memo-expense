package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.dispatcher.LoginDispatcher
import com.example.go.memoexpensesapplication.model.User
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class FragmentLoginViewModel @Inject constructor(
    dispatcher: LoginDispatcher
) : ViewModel() {

    private var mLoginNavigator: FragmentLoginNavigator? = null

    val mail: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    private val login: Disposable
    private val _authenticationFail = PublishProcessor.create<Exception?>()
    val authenticationFail: Flowable<Exception?> = _authenticationFail
    private val _createUserFail = PublishProcessor.create<Exception?>()
    val createUserFail: Flowable<Exception?> = _createUserFail

    init {
        login = dispatcher.onLogin
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    mLoginNavigator?.onLoggedIn(user)
                        ?: throw RuntimeException("Navigator must be set")
                },
                { throw it }
            )
        dispatcher.onCreateUserFail
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_createUserFail)
        dispatcher.onAuthenticationFail
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_authenticationFail)
    }

    fun setLoginNavigator(navigator: FragmentLoginNavigator) {
        this.mLoginNavigator = navigator
    }

    override fun onCleared() {
        login.dispose()
        super.onCleared()
    }

    interface FragmentLoginNavigator {
        fun onLoggedIn(user: User)
    }
}