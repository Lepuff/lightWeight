package com.example.lightweight.ui.Feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweight.classes.AbstractWorkout

class FeedViewModel : ViewModel() {

    val workoutList = MutableLiveData<MutableList<AbstractWorkout>>().apply {
        value = ArrayList()
    }
}