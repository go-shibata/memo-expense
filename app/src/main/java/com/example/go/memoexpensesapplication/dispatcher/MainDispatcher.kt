package com.example.go.memoexpensesapplication.dispatcher

import com.example.go.memoexpensesapplication.action.MainAction
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

object MainDispatcher: Dispatcher<MainAction<*>> {
    private val onChangeExpenseProcessor: FlowableProcessor<MainAction<*>> = PublishProcessor.create()
    private val onChangeExpense: Flowable<MainAction<*>> = onChangeExpenseProcessor

    override fun dispatch(action: MainAction<*>) {
        onChangeExpenseProcessor.onNext(action)
    }

    fun subscribe(block: (MainAction<*>) -> Unit): Disposable = onChangeExpense.subscribe(block)
}