package com.example.lightweight.ui.NewWorkout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class NewGymWorkoutViewModel : ViewModel() {

    private var exerciseLiveData = MutableLiveData<MutableList<Exercise>>()
    private var exerciseList : MutableList<Exercise> = ArrayList()

    fun init(){
        exerciseLiveData.value  = exerciseList
    }

    fun getExerciseList() = exerciseLiveData

}