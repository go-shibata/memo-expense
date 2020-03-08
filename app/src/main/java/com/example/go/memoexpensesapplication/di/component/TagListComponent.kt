package com.example.go.memoexpensesapplication.di.component

import com.example.go.memoexpensesapplication.actioncreator.TagListActionCreator
import com.example.go.memoexpensesapplication.di.module.PreferencesModule
import com.example.go.memoexpensesapplication.fragment.TagListFragment
import com.example.go.memoexpensesapplication.viewmodel.FragmentTagListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PreferencesModule::class])
interface TagListComponent {
    fun inject(actionCreator: TagListActionCreator)
    fun inject(viewModel: FragmentTagListViewModel)
    fun inject(fragment: TagListFragment)
}