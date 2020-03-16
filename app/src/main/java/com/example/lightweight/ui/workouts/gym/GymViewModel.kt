package com.example.lightweight.ui.workouts.gym

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweight.classes.Exercise


class GymViewModel : ViewModel() {

    var exerciseLiveData = MutableLiveData<MutableList<Exercise>>().apply {
        value = ArrayList()
    }
    var title = MutableLiveData<String>().apply {
        value = null
    }
    var date = MutableLiveData<String>().apply {
        value = null
    }
    var isLoadedFromDb = MutableLiveData<Boolean>().apply {
        value = false // todo fix var names?
    }
    var isInEditState  = MutableLiveData<Boolean>().apply {
        value = false
    }
}