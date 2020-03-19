package com.example.lightweight.ui.Profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {


    var isInEditState = MutableLiveData<Boolean>().apply {
        value = false
    }




}
