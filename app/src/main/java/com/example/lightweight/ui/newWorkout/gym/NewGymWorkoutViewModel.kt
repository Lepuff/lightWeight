package com.example.lightweight.ui.newWorkout.gym

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweight.classes.Exercise


class NewGymWorkoutViewModel : ViewModel() {

    private var exerciseLiveData = MutableLiveData<MutableList<Exercise>>()
    private var exerciseList : MutableList<Exercise> = ArrayList()

    fun init(){
        exerciseLiveData.value  = exerciseList
    }

    fun getExerciseList() = exerciseLiveData

}