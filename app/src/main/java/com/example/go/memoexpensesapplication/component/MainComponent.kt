package com.example.go.memoexpensesapplication.component

import com.example.go.memoexpensesapplication.actioncreator.MainActionCreator
import com.example.go.memoexpensesapplication.fragment.MainFragment
import com.example.go.memoexpensesapplication.module.DatabaseModule
import com.example.go.memoexpensesapplication.module.MainModule
import com.example.go.memoexpensesapplication.module.PreferencesModule
import com.example.go.memoexpensesapplication.viewmodel.FragmentMainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, PreferencesModule::class, MainModule::class])
interface MainComponent {
    fun inject(actionCreator: MainActionCreator)
    fun inject(mainViewModel: FragmentMainViewModel)
    fun inject(fragment: MainFragment)
}