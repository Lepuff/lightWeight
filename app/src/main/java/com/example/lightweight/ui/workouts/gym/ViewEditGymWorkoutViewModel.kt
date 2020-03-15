package com.example.lightweight.ui.workouts.gym

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweight.classes.Exercise

class ViewEditGymWorkoutViewModel : ViewModel() {

    val exerciseList = MutableLiveData<MutableList<Exercise>>().apply {
        value = ArrayList()
    }
    

}