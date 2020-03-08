package com.example.go.memoexpensesapplication.di.module

import com.example.go.memoexpensesapplication.dispatcher.LoginDispatcher
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoginModule {

    @Provides
    @Singleton
    fun provideLoginDispatcher(): LoginDispatcher = LoginDispatcher()
}