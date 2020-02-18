package com.example.lightweight

import android.text.TextUtils
import android.util.Patterns

object Validation {
    fun isFieldEmpty(target: CharSequence): Boolean {
        return TextUtils.isEmpty(target)
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isValidPassword(target: CharSequence?): Boolean {
        return target!!.length > 5
    }
}