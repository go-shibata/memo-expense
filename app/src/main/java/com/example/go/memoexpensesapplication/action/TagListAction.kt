package com.example.go.memoexpensesapplication.action

sealed class TagListAction<out T>(val data: T) : Action {
    class GetTag : TagListAction<Unit>(Unit)
    class AddTag(data: String) : TagListAction<String>(data)
    class DeleteTag(data: String) : TagListAction<String>(data)
}