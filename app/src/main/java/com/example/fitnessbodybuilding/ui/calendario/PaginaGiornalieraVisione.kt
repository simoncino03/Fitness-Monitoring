package com.example.fitnessbodybuilding.ui.calendario

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.fitnessbodybuilding.R
import com.example.fitnessbodybuilding.ui.ClassiEsecutive.MyAppGlobals
import com.google.firebase.firestore.FirebaseFirestore

class PaginaGiornalieraVisione : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    private var selectCibo: String? = null
    private lateinit var ciboSpinner: Spinner
    private lateinit var info: TextView
    private lateinit var data: String
    private lateinit var giorno: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_giornaliera_visione)

        // Inizializzazione delle view
        ciboSpinner = findViewById(R.id.ciboSpinner)
        info = findViewById(R.id.infoCibo)

        val all = findViewById<TextView>(R.id.schedaSvolta)
        data = intent.getStringExtra("Giorno").toString()
        giorno = findViewById(R.id.giornoSelezionato)
        giorno.text = data

        val btn = findViewById<Button>(R.id.btnInd)
        btn.setOnClickListener {
            finish()
        }

        populateCibi(data, MyAppGlobals.getGlobalVariableEmail())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Recap Giornaliero"

        val email = MyAppGlobals.getGlobalVariableEmail()
        val allenamentiSvolti = db.collection("Allenamento").whereEqualTo("dataAll", data).whereEqualTo("email", email)
        allenamentiSvolti.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                all.text = "Non ti sei allenato questo giorno"
            } else {
                var nScheda = 0
                var idSch = ""
                for (document in querySnapshot.documents) {
                    idSch += document.getString("idScheda")
                }
                nScheda = when (idSch) {
                    "idScheda1" -> 1
                    "idScheda2" -> 2
                    else -> 3
                }
                all.text = "Hai svolto la scheda num: $nScheda"
            }
        }
    }

    private fun populateCibi(date: String, gmail: String) {
        val tutto = db.collection("Alimentazione").whereEqualTo("dataMangiato", date).whereEqualTo("email", gmail)
        tutto.get().addOnSuccessListener { result ->
            val nomeCibi = ArrayList<String>()
            val infoCibi = HashMap<String, Map<String, String>>()

            for (document in result) {
                val nomeCibo = document.getString("nomeAlimento")
                val mom = document.getString("periodoGiornata")
                val weight = document.getString("peso")
                Log.d("FirebaseData", "nomeCibo: $nomeCibo, mom: $mom, weight: $weight")
                if (nomeCibo != null && mom != null && weight != null) {
                    nomeCibi.add(nomeCibo)
                    val exerciseInfo = mapOf(
                        "peso" to weight,
                        "periodoGiornata" to mom
                    )
                    infoCibi[nomeCibo] = exerciseInfo
                }
            }

            if (nomeCibi.isEmpty()) {
                info.text = "Non hai mangiato nulla durante questo giorno"
            } else {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    nomeCibi
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ciboSpinner.adapter = adapter

                ciboSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        selectCibo = parent.getItemAtPosition(position).toString()
                        val exerciseInfo = infoCibi[selectCibo]
                        if (exerciseInfo != null) {
                            val periodo = exerciseInfo["periodoGiornata"]
                            val weight = exerciseInfo["peso"]
                            val stringaCompleta = "Periodo: $periodo / Peso: $weight"
                            info.text = stringaCompleta
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Se non è stato selezionato nulla, non fare nulla
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("FirebaseError", "Errore nel recupero dei dati: ", exception)
            info.text = "Errore nel recupero dei dati"
        }
    }
}
