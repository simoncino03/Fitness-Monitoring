package com.example.fitnessbodybuilding.ui.calendario

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnessbodybuilding.R

class PaginaGiornalieraVisione : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_giornaliera_visione)
        val data=intent.getStringExtra("Giorno")
        val giorno=findViewById<TextView>(R.id.giornoSelezionato)
        giorno.text=data

    }
}