package jp.kiroru.kotlintask02

import android.content.Context
import androidx.preference.PreferenceManager


const val FIRST = "first"

fun isFirst(context: Context) : Boolean {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.getBoolean(FIRST, true)
}

fun markFirst(context: Context) {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = pref.edit()
    editor.putBoolean(FIRST, false)
    editor.commit()
}