package com.example.lightweight.ui.newWorkout.cycling

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewCyclingWorkoutViewModel : ViewModel() {


    var averageSpeed = MutableLiveData<Float>().apply {
        value = null
    }
    var topSpeed = MutableLiveData<Float>().apply {
        value = null
    }
    var totalTime = MutableLiveData<Float>().apply {
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
    var maxForce= MutableLiveData<Int>().apply {
        value = null
    }
    var averageCadence= MutableLiveData<Int>().apply {
        value = null
    }
    var maxCadence= MutableLiveData<Int>().apply {
        value = null
    }
    var calories= MutableLiveData<Int>().apply {
        value = null
    }
}