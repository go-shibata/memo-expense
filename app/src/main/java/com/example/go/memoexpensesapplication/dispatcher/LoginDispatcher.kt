package com.example.go.memoexpensesapplication.dispatcher

import com.example.go.memoexpensesapplication.action.LoginAction
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor

class LoginDispatcher : Dispatcher<LoginAction<*>> {

    private val dispatcherLogin: FlowableProcessor<LoginAction.Login> = BehaviorProcessor.create()
    val onLogin: Flowable<LoginAction.Login> = dispatcherLogin

    private val dispatcherAuthenticationFail: FlowableProcessor<LoginAction.AuthenticationFail> =
        BehaviorProcessor.create()
    val onAuthenticationFail: Flowable<LoginAction.AuthenticationFail> =
        dispatcherAuthenticationFail

    override fun dispatch(action: LoginAction<*>) {
        when (action) {
            is LoginAction.Login -> dispatcherLogin.onNext(action)
            is LoginAction.AuthenticationFail -> dispatcherAuthenticationFail.onNext(action)
        }
    }
}