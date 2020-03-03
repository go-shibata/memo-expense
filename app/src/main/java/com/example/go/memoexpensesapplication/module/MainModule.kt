package com.example.go.memoexpensesapplication.module

import com.example.go.memoexpensesapplication.dispatcher.MainDispatcher
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun provideMainDispatcher(): MainDispatcher = MainDispatcher()
}