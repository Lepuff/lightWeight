package com.example.lightweight.ui.Social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SocialViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is social fragment"
    }
    val text: LiveData<String> = _text
}