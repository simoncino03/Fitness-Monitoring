package com.example.fitnessbodybuilding.ui.schedaEs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SchedaEsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Questo è il frammento per le schede"
    }
    val text: LiveData<String> = _text
}