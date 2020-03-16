package com.example.lightweight.ui.workouts.gym

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweight.classes.Exercise


class GymViewModel : ViewModel() {

    var exerciseLiveData = MutableLiveData<MutableList<Exercise>>().apply {
        value = ArrayList()
    }
}