package com.example.go.memoexpensesapplication.dispatcher

import com.example.go.memoexpensesapplication.action.LoginAction
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginDispatcher @Inject constructor() : Dispatcher<LoginAction<*>> {

    private val dispatcherLogin: FlowableProcessor<LoginAction.Login> = BehaviorProcessor.create()
    val onLogin: Flowable<LoginAction.Login> = dispatcherLogin

    private val dispatcherAuthenticationFail: FlowableProcessor<LoginAction.AuthenticationFail> =
        BehaviorProcessor.create()
    val onAuthenticationFail: Flowable<LoginAction.AuthenticationFail> =
        dispatcherAuthenticationFail

    private val dispatcherAutoLoginFail: FlowableProcessor<LoginAction.AutoLoginFail> =
        BehaviorProcessor.create()
    val onAutoLoginFail: Flowable<LoginAction.AutoLoginFail> = dispatcherAutoLoginFail

    override fun dispatch(action: LoginAction<*>) {
        when (action) {
            is LoginAction.Login -> dispatcherLogin.onNext(action)
            is LoginAction.AuthenticationFail -> dispatcherAuthenticationFail.onNext(action)
            is LoginAction.AutoLoginFail -> dispatcherAutoLoginFail.onNext(action)
        }
    }
}