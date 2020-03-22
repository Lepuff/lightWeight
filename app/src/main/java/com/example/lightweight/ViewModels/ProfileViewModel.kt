package com.example.lightweight.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {


    var isInEditState = MutableLiveData<Boolean>().apply {
        value = false
    }




}
