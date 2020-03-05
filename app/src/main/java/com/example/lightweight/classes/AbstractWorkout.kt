package com.example.lightweight.classes


import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData

abstract class AbstractWorkout(
    val icon: Int
) {
    abstract var id : String?
    abstract var title: String?
    abstract var date: String?



    fun getWorkoutTitle() : String? {
        return title
    }
    fun setWorkoutTitle(newTitle: String){
        title = newTitle
    }

    fun getWorkoutList(){
    }



    abstract fun showWorkout(context: Context)
    abstract fun editWorkout()
    abstract fun newWorkout(context: Context)



}