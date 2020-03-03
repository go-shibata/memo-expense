package com.example.go.memoexpensesapplication.actioncreator

import com.example.go.memoexpensesapplication.Preferences
import com.example.go.memoexpensesapplication.action.TagListAction
import com.example.go.memoexpensesapplication.dispatcher.TagListDispatcher
import javax.inject.Inject

class TagListActionCreator @Inject constructor(
    private val pref: Preferences,
    private val dispatcher: TagListDispatcher
) {

    fun getAllTags() {
        val tags = pref.getTags().toList()
        dispatcher.dispatch(TagListAction.GetAllTags(tags))
    }

    fun addTag(tag: String) {
        pref.addTags(tag)
        dispatcher.dispatch(TagListAction.AddTag(tag))
    }

    fun deleteTag(tag: String) {
        pref.removeTags(tag)
        dispatcher.dispatch(TagListAction.DeleteTag(tag))
    }
}