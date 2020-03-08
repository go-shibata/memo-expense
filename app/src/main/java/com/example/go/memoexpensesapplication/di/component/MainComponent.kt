package com.example.go.memoexpensesapplication.di.component

import com.example.go.memoexpensesapplication.actioncreator.MainActionCreator
import com.example.go.memoexpensesapplication.di.module.DatabaseModule
import com.example.go.memoexpensesapplication.di.module.PreferencesModule
import com.example.go.memoexpensesapplication.fragment.MainFragment
import com.example.go.memoexpensesapplication.viewmodel.FragmentMainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, PreferencesModule::class])
interface MainComponent {
    fun inject(actionCreator: MainActionCreator)
    fun inject(mainViewModel: FragmentMainViewModel)
    fun inject(fragment: MainFragment)
}