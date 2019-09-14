package com.example.go.memoexpensesapplication.model

import com.example.go.memoexpensesapplication.constant.ExpenseViewType

data class Expense(
    var id: String?,
    val type: ExpenseViewType,
    val tag: String?,
    val value: Int?,
    val note: String?
) {
    constructor(
        tag: String?,
        value: Int?,
        note: String?
    ): this(null, ExpenseViewType.BODY, tag, value, note)
}