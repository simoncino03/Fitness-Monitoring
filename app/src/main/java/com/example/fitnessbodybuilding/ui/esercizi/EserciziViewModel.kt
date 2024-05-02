package com.example.fitnessbodybuilding.ui.esercizi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EserciziViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Questo è il fragment di tutti gli esercizi esistenti"
    }
    val text: LiveData<String> = _text
}