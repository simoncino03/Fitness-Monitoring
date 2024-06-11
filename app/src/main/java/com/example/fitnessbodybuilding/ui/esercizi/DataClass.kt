package com.example.fitnessbodybuilding.ui.esercizi

class DataClass(selectedExercise: String) {
    private var title: String = selectedExercise

    fun getDataTitle(): String {
        return title
    }
}
