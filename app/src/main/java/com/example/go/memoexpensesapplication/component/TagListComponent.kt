package com.example.go.memoexpensesapplication.component

import com.example.go.memoexpensesapplication.actioncreator.TagListActionCreator
import com.example.go.memoexpensesapplication.fragment.TagListFragment
import com.example.go.memoexpensesapplication.module.PreferencesModule
import com.example.go.memoexpensesapplication.module.TagListModule
import com.example.go.memoexpensesapplication.viewmodel.FragmentTagListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PreferencesModule::class, TagListModule::class])
interface TagListComponent {
    fun inject(actionCreator: TagListActionCreator)
    fun inject(viewModel: FragmentTagListViewModel)
    fun inject(fragment: TagListFragment)
}