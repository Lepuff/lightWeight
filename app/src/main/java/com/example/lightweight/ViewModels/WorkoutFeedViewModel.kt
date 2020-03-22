package com.example.lightweight.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweight.classes.Workout

class WorkoutFeedViewModel : ViewModel() {

    val workoutList = MutableLiveData<MutableList<Workout>>().apply {
        value = ArrayList()
    }

    fun clear(){
        workoutList.value?.clear()
    }
}