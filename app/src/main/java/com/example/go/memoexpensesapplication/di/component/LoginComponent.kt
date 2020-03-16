package com.example.go.memoexpensesapplication.di.component

import com.example.go.memoexpensesapplication.actioncreator.LoginActionCreator
import com.example.go.memoexpensesapplication.di.module.FirebaseModule
import com.example.go.memoexpensesapplication.view.fragment.LoginFragment
import com.example.go.memoexpensesapplication.view.fragment.SplashFragment
import com.example.go.memoexpensesapplication.viewmodel.FragmentLoginViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseModule::class])
interface LoginComponent {
    fun inject(creator: LoginActionCreator)
    fun inject(viewModel: FragmentLoginViewModel)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: SplashFragment)
}