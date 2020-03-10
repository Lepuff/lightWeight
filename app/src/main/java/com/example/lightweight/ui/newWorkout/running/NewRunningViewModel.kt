package com.example.lightweight.ui.newWorkout.running

import androidx.lifecycle.MutableLiveData

class NewRunningViewModel {

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

    var averageForce = MutableLiveData<Int>().apply {
        value = null
    }

    var maxForce = MutableLiveData<Int>().apply {
        value = null
    }

    var calories = MutableLiveData<Int>().apply {
        value = null
    }

}