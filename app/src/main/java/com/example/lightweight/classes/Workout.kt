package com.example.lightweight.classes


import android.content.Context
import android.net.Uri
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import java.net.URI

abstract class Workout(
    val icon: Int
) {
    abstract var id: String
    abstract var title: String
    abstract var date: String
    abstract var userName: String
    abstract var userImage: String?
    abstract var userId: String

    abstract fun viewWorkout(context: Context)
    abstract fun newWorkout(context: Context)
}