package com.example.fitnessbodybuilding.ui.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Questo è il frammento del timer"
    }
    val text: LiveData<String> = _text
}