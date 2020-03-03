package com.example.lightweight.ui.Feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweight.classes.AbstractWorkout

class FeedViewModel : ViewModel() {

    private val workoutLiveData = MutableLiveData<MutableList<AbstractWorkout>>()
    private var workoutList : MutableList<AbstractWorkout> = ArrayList()


    fun init(){
        workoutLiveData.value = workoutList
    }

    fun getExerciseList() = workoutLiveData

    fun setExerciseList(newWorkoutList: MutableList<AbstractWorkout>){
        workoutList = newWorkoutList
        init()
    }
}