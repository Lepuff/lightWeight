package com.example.lightweight.ui.workoutDetails.Gym

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweight.classes.Exercise

class ViewEditGymWorkoutViewModel : ViewModel() {

    private var exerciseLiveDate = MutableLiveData<MutableList<Exercise>>()
    private var exerciseList : MutableList<Exercise> = ArrayList()
    
    fun init(){
        exerciseLiveDate.value = exerciseList
    }
    fun getExerciseList() = exerciseLiveDate
}