package com.example.go.memoexpensesapplication.dispatcher

import com.example.go.memoexpensesapplication.action.TagListAction
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor

class TagListDispatcher : Dispatcher<TagListAction<*>> {

    private val dispatcherGetAllTags: FlowableProcessor<TagListAction.GetAllTags> =
        BehaviorProcessor.create<TagListAction.GetAllTags>()
    val onGetAllTags: Flowable<TagListAction.GetAllTags> = dispatcherGetAllTags

    private val dispatcherAddTag: FlowableProcessor<TagListAction.AddTag> =
        BehaviorProcessor.create<TagListAction.AddTag>()
    val onAddTag: Flowable<TagListAction.AddTag> = dispatcherAddTag

    private val dispatcherDeleteTag: FlowableProcessor<TagListAction.DeleteTag> =
        BehaviorProcessor.create<TagListAction.DeleteTag>()
    val onDeleteTag: Flowable<TagListAction.DeleteTag> = dispatcherDeleteTag

    override fun dispatch(action: TagListAction<*>) {
        when (action) {
            is TagListAction.GetAllTags -> dispatcherGetAllTags.onNext(action)
            is TagListAction.AddTag -> dispatcherAddTag.onNext(action)
            is TagListAction.DeleteTag -> dispatcherDeleteTag.onNext(action)
        }
    }
}