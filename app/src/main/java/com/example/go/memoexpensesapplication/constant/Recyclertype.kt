package com.example.go.memoexpensesapplication.constant

enum class RecyclerType(val type: Int) {
    HEADER(0),
    FOOTER(1),
    SECTION(2),
    BODY(3);

    companion object {
        fun fromInt(type: Int): RecyclerType {
            return values().single {
                it.type == type
            }
        }
    }
}