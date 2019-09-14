package com.example.go.memoexpensesapplication.constant

enum class ExpenseViewType(val type: Int) {
    HEADER(0),
    FOOTER(1),
    SECTION(2),
    BODY(3);

    companion object {
        fun fromInt(type: Int): ExpenseViewType {
            return values().single {
                it.type == type
            }
        }
    }
}