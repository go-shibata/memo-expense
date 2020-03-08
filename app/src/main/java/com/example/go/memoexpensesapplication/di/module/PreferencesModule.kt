package com.example.go.memoexpensesapplication.di.module

import com.example.go.memoexpensesapplication.Application
import com.example.go.memoexpensesapplication.Preferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule {

    @Provides
    @Singleton
    fun providePrefs(): Preferences = Preferences(Application.getContext())
}