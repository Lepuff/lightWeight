package com.example.lightweight.classes


import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData

abstract class AbstractWorkout(
    val icon: Int
) {
    //ToDo add id?
    abstract var title: String?
    abstract var date: String?
    abstract var image: String? //ToDo remove!!!: it is for test


    fun getWorkoutTitle() : String? {
        return title
    }
    fun setWorkoutTitle(newTitle: String){
        title = newTitle
    }





    abstract fun showWorkout(context: Context)
    abstract fun editWorkout()
    abstract fun newWorkout(context: Context)
    abstract fun addWorkoutToDb(
        workoutTitle: Editable,
        workoutDate: Editable,
        exerciseList: MutableList<Exercise>,
        exerciseLiveData: MutableLiveData<MutableList<Exercise>>)



}