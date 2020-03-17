package com.example.lightweight.ui.Feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweight.classes.AbstractWorkout

class WorkoutFeedViewModel : ViewModel() {

    val workoutList = MutableLiveData<MutableList<AbstractWorkout>>().apply {
        value = ArrayList()
    }

    fun clear(){
        workoutList.value?.clear()
    }
}