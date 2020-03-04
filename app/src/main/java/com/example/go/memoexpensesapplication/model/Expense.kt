package com.example.go.memoexpensesapplication.model

import com.example.go.memoexpensesapplication.constant.ExpenseViewType

data class Expense(
    var id: String?,
    val type: ExpenseViewType,
    val uid: String?,
    val tag: String?,
    val value: Int?,
    val note: String?
) {
    constructor(
        uid: String?,
        tag: String?,
        value: Int?,
        note: String?
    ) : this(null, ExpenseViewType.BODY, uid, tag, value, note)
}