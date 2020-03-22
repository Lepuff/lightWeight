package com.example.lightweight

import android.provider.Settings.Global.getString
import android.text.TextUtils
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText

object Validation {
    private const val MIN_PASSWORD_LENGTH = 5


    fun isValidEmail(target: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isValidPassword(target: CharSequence?): Boolean {
        return target!!.length > MIN_PASSWORD_LENGTH
    }
}