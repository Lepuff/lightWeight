package com.example.lightweight.ui.Profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    var profileName = MutableLiveData<String>().apply {
        value = null
    }
    var profileAge = MutableLiveData<Int>().apply {
        value = null
    }
    var profileEmail = MutableLiveData<String>().apply {
        value = null
    }
     var profileNewPassword = MutableLiveData<String>().apply {
         value = null
     }
    var profileConfirmPassword = MutableLiveData<String>().apply {
        value = null
    }
}
