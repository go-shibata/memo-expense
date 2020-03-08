package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.dispatcher.TagListDispatcher
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class FragmentTagListViewModel @Inject constructor(
    dispatcher: TagListDispatcher
) : ViewModel() {

    private val _tags = PublishProcessor.create<List<String>>()
    val tags: Flowable<List<String>> = _tags

    private val _addTag = PublishProcessor.create<String>()
    val addTag: Flowable<String> = _addTag

    private val _deleteTag = PublishProcessor.create<String>()
    val deleteTag: Flowable<String> = _deleteTag

    init {
        dispatcher.onGetAllTags
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_tags)
        dispatcher.onAddTag
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_addTag)
        dispatcher.onDeleteTag
            .map { action -> action.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_deleteTag)
    }
}