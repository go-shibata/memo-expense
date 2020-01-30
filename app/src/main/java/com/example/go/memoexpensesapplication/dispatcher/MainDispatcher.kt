package com.example.go.memoexpensesapplication.dispatcher

import com.example.go.memoexpensesapplication.action.Action
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

object MainDispatcher : Dispatcher<Action> {
    private val onChangeExpenseProcessor: FlowableProcessor<Action> = PublishProcessor.create()
    private val onChangeExpense: Flowable<Action> = onChangeExpenseProcessor

    override fun dispatch(action: Action) {
        onChangeExpenseProcessor.onNext(action)
    }

    fun subscribe(block: (Action) -> Unit): Disposable = onChangeExpense.subscribe(block)
}