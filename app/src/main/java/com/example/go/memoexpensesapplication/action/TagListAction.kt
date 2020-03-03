package com.example.go.memoexpensesapplication.action

sealed class TagListAction<out T>(val data: T) : Action {

    class GetAllTags(tags: List<String>) : TagListAction<List<String>>(tags)
    class AddTag(tag: String) : TagListAction<String>(tag)
    class DeleteTag(tag: String) : TagListAction<String>(tag)
}