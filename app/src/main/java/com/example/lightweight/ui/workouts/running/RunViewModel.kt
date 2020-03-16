package com.example.lightweight.ui.workouts.running

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RunViewModel : ViewModel() {


    var title  = MutableLiveData<String>().apply {
        value = null
    }
    var date = MutableLiveData<String>().apply {
        value = null
    }

    var distance = MutableLiveData<Float>().apply {
        value = null
    }

    var totalTime = MutableLiveData<Float>().apply {
        value = null
    }

    var averageSpeed = MutableLiveData<Float>().apply {
        value = null
    }

    var topSpeed = MutableLiveData<Float>().apply {
        value = null
    }

    var averagePulse = MutableLiveData<Int>().apply {
        value = null
    }

    var maxPulse = MutableLiveData<Int>().apply {
        value = null
    }

    var calories = MutableLiveData<Int>().apply {
        value = null
    }
    var alreadyLoaded = MutableLiveData<Boolean>().apply {
        value = false // todo fix var names?
    }


}