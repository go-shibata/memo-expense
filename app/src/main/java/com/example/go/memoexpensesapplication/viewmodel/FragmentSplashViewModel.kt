package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.dispatcher.LoginDispatcher
import com.example.go.memoexpensesapplication.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class FragmentSplashViewModel @Inject constructor(
    dispatcher: LoginDispatcher
) : ViewModel() {
    private var mSplashNavigator: FragmentSplashNavigator? = null

    private val mCompositeDisposable = CompositeDisposable()

    private val mLogin: Disposable
    private val mAutoLoginFail: Disposable

    init {
        mLogin = dispatcher.onLogin
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { user ->
                mSplashNavigator?.onLoggedIn(user)
                    ?: throw RuntimeException("Navigator must be set")
            }
            .addTo(mCompositeDisposable)
        mAutoLoginFail = dispatcher.onAutoLoginFail
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mSplashNavigator?.onAutoLoginFailed()
                    ?: throw RuntimeException("Navigator must be set")
            }
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
    }

    fun setSplashNavigator(navigator: FragmentSplashNavigator) {
        this.mSplashNavigator = navigator
    }

    interface FragmentSplashNavigator {
        fun onAutoLoginFailed()
        fun onLoggedIn(user: User)
    }
}