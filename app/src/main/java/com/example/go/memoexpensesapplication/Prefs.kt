package com.example.go.memoexpensesapplication

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.go.memoexpensesapplication.constant.PrefsKey

object Prefs {

    private lateinit var pref: SharedPreferences

    fun initPrefs(context: Context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getTags(): Set<String> = pref.getStringSet(PrefsKey.TAG.key, null) ?: emptySet()

    fun addTags(tag: String) {
        val tags = getTags()
        pref.edit()
            .putStringSet(PrefsKey.TAG.key, tags.plus(tag))
            .apply()
    }
    fun removeTags(tag: String) {
        val tags = getTags()
        pref.edit()
            .putStringSet(PrefsKey.TAG.key, tags.minus(tag))
            .apply()
    }
}