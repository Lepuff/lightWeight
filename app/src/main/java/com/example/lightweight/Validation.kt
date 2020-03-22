package com.example.lightweight


import android.util.Patterns


object Validation {
    private const val MIN_PASSWORD_LENGTH = 5


    fun isValidEmail(target: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isValidPassword(target: CharSequence?): Boolean {
        return target!!.length > MIN_PASSWORD_LENGTH
    }
}