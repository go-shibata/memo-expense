package com.example.go.memoexpensesapplication.dispatcher

import com.example.go.memoexpensesapplication.action.MainAction
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainDispatcher @Inject constructor() : Dispatcher<MainAction<*>> {

    private val dispatcherGetAllExpenses: FlowableProcessor<MainAction.GetAllExpenses> =
        BehaviorProcessor.create()
    val onGetAllExpenses: Flowable<MainAction.GetAllExpenses> = dispatcherGetAllExpenses

    private val dispatcherAddExpense: FlowableProcessor<MainAction.AddExpense> =
        BehaviorProcessor.create()
    val onAddExpense: Flowable<MainAction.AddExpense> = dispatcherAddExpense

    private val dispatcherEditExpense: FlowableProcessor<MainAction.EditExpense> =
        BehaviorProcessor.create()
    val onEditExpense: Flowable<MainAction.EditExpense> = dispatcherEditExpense

    private val dispatcherDeleteExpense: FlowableProcessor<MainAction.DeleteExpense> =
        BehaviorProcessor.create()
    val onDeleteExpense: Flowable<MainAction.DeleteExpense> = dispatcherDeleteExpense

    private val dispatcherMoveToTagList: FlowableProcessor<MainAction.MoveToTagList> =
        BehaviorProcessor.create()
    val onMoveToTagList: Flowable<MainAction.MoveToTagList> = dispatcherMoveToTagList

    private val dispatcherToggleCheckable: FlowableProcessor<MainAction.ToggleCheckable> =
        BehaviorProcessor.create()
    val onToggleCheckable: Flowable<MainAction.ToggleCheckable> = dispatcherToggleCheckable

    override fun dispatch(action: MainAction<*>) {
        when (action) {
            is MainAction.GetAllExpenses -> dispatcherGetAllExpenses.onNext(action)
            is MainAction.AddExpense -> dispatcherAddExpense.onNext(action)
            is MainAction.EditExpense -> dispatcherEditExpense.onNext(action)
            is MainAction.DeleteExpense -> dispatcherDeleteExpense.onNext(action)
            is MainAction.MoveToTagList -> dispatcherMoveToTagList.onNext(action)
            is MainAction.ToggleCheckable -> dispatcherToggleCheckable.onNext(action)
        }
    }
}