package jp.kiroru.kotlintask02

import android.content.Context
//import android.preference.PreferenceManager


const val FIRST = "first"

fun isFirst(context: Context) : Boolean {
    //val pref = androidx.preference.PreferenceFragmentCompat
    //val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.getBoolean(FIRST, true)     // 無ければ true を返す
}

fun markFirst(context: Context) {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = pref.edit()
    editor.putBoolean(FIRST, false)         // false にマークする
    editor.commit()
}

