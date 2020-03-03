package com.example.go.memoexpensesapplication.module

import com.example.go.memoexpensesapplication.network.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(): Database = Database()
}