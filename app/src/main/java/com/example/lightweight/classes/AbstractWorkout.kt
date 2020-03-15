package com.example.lightweight.classes


import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData

abstract class AbstractWorkout(
    val icon: Int
) {
    abstract var id : String
    abstract var title: String
    abstract var date: String

    abstract fun showWorkout(context: Context)
    abstract fun newWorkout(context: Context)

}