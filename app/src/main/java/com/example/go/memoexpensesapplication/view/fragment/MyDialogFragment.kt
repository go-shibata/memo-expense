package com.example.go.memoexpensesapplication.view.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MyDialogFragment : DialogFragment() {

    private var builder: AlertDialog.Builder? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return builder?.create() ?: super.onCreateDialog(savedInstanceState)
    }

    fun setBuilder(builder: AlertDialog.Builder): MyDialogFragment {
        this.builder = builder
        return this
    }
}