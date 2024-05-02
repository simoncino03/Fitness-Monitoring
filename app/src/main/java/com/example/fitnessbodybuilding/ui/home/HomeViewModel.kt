package com.example.fitnessbodybuilding.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Questo è iul frammento base"
    }
    val text: LiveData<String> = _text
}