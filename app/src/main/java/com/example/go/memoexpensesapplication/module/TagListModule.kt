package com.example.go.memoexpensesapplication.module

import com.example.go.memoexpensesapplication.dispatcher.TagListDispatcher
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TagListModule {

    @Provides
    @Singleton
    fun provideTagListDispatcher(): TagListDispatcher = TagListDispatcher()
}