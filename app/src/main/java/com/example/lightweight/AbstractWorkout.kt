package com.example.lightweight


import android.content.Context
import java.util.*

abstract class AbstractWorkout(
    val icon: Int
) {

    abstract var title: String
    abstract var date: Date
    abstract var image: String //ToDo remove!!!: it is for test


    fun getWorkoutTitle() : String{
        return title
    }
    fun setWorkoutTitle(newTitle: String){
        title = newTitle
    }

    fun getWorkoutDate (): Date{
        return date
    }
    fun setWorokoutDate(newDate: Date){
        date = newDate
    }


    abstract fun showWorkout(context: Context)
    abstract fun editWorkout()


}