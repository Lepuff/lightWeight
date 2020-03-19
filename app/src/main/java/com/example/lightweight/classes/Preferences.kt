package com.example.lightweight.classes

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Preferences(context: Context) {
    val SP_INFO = "SP_INFO"
    val mSharedPreferences = context.getSharedPreferences(SP_INFO, MODE_PRIVATE)

    var firstTimeLogin: Boolean
        get() = mSharedPreferences.getBoolean("firstTime", true)
        set(bool) = mSharedPreferences.edit().putBoolean("firstTime", bool).apply()
}