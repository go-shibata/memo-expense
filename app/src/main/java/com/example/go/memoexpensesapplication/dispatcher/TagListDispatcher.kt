package com.example.go.memoexpensesapplication.dispatcher

import com.example.go.memoexpensesapplication.action.TagListAction
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagListDispatcher @Inject constructor() : Dispatcher<TagListAction<*>> {

    private val dispatcherGetAllTags: FlowableProcessor<TagListAction.GetAllTags> =
        BehaviorProcessor.create()
    val onGetAllTags: Flowable<TagListAction.GetAllTags> = dispatcherGetAllTags

    private val dispatcherAddTag: FlowableProcessor<TagListAction.AddTag> =
        BehaviorProcessor.create()
    val onAddTag: Flowable<TagListAction.AddTag> = dispatcherAddTag

    private val dispatcherDeleteTag: FlowableProcessor<TagListAction.DeleteTag> =
        BehaviorProcessor.create()
    val onDeleteTag: Flowable<TagListAction.DeleteTag> = dispatcherDeleteTag

    override fun dispatch(action: TagListAction<*>) {
        when (action) {
            is TagListAction.GetAllTags -> dispatcherGetAllTags.onNext(action)
            is TagListAction.AddTag -> dispatcherAddTag.onNext(action)
            is TagListAction.DeleteTag -> dispatcherDeleteTag.onNext(action)
        }
    }
}