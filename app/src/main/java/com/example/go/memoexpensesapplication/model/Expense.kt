package com.example.go.memoexpensesapplication.model

data class Expense(
    var id: String?,
    val uid: String,
    val tag: String,
    val value: Int,
    val note: String
) {
    constructor(
        uid: String,
        tag: String,
        value: Int,
        note: String
    ) : this(null, uid, tag, value, note)
}