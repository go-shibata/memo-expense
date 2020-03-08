package com.example.go.memoexpensesapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.go.memoexpensesapplication.di.component.TagListComponent
import com.example.go.memoexpensesapplication.dispatcher.TagListDispatcher
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class FragmentTagListViewModel : ViewModel() {

    @Inject
    lateinit var dispatcher: TagListDispatcher

    private val _tags = PublishProcessor.create<List<String>>()
    val tags: Flowable<List<String>> = _tags

    private val _addTag = PublishProcessor.create<String>()
    val addTag: Flowable<String> = _addTag

    private val _deleteTag = PublishProcessor.create<String>()
    val deleteTag: Flowable<String> = _deleteTag

    fun inject(tagListComponent: TagListComponent) {
        tagListComponent.inject(this)
        subscribe()
    }

    private fun subscribe() {
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