package com.example.go.memoexpensesapplication.model

import com.example.go.memoexpensesapplication.Application
import com.example.go.memoexpensesapplication.R
import com.google.firebase.auth.FirebaseUser
import java.io.Serializable

data class User(
    val name: String,
    val mail: String,
    val uid: String
) : Serializable {
    constructor(user: FirebaseUser) : this(
        user.displayName ?: Application.getContext().getString(R.string.user_no_name),
        user.email ?: Application.getContext().getString(R.string.user_no_mail),
        user.uid
    )
}