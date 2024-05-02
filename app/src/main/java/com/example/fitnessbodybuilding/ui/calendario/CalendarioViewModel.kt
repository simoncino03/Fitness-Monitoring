package com.example.fitnessbodybuilding.ui.calendario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarioViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Questo è il frammento del calendario"
    }
    val text: LiveData<String> = _text
}