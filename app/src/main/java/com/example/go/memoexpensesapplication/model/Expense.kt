package com.example.go.memoexpensesapplication.model

import com.example.go.memoexpensesapplication.constant.RecyclerType

data class Expense(
    var id: String?,
    val type: RecyclerType,
    val tag: String?,
    val value: Int?,
    val note: String?
) {
    constructor(
        tag: String?,
        value: Int?,
        note: String?
    ): this(null, RecyclerType.BODY, tag, value, note)
}