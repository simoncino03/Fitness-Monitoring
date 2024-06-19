package com.example.fitnessbodybuilding.ui.calendario

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class PaginaGiornalieraVisione : AppCompatActivity() {
    val db=FirebaseFirestore.getInstance()
    private var selectedExercise: String? = null
    val ciboSpinner = findViewById<Spinner>(R.id.ciboSpinner)
    val info=findViewById<TextView>(R.id.infoCibo)
    private lateinit var data:String
    private lateinit var giorno:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_giornaliera_visione)
        val all=findViewById<TextView>(R.id.schedaSvolta)
        data = intent.getStringExtra("Giorno").toString()
        giorno = findViewById<TextView>(R.id.giornoSelezionato)
        giorno.text = data
        val btn=findViewById<Button>(R.id.btnInd)
        btn.setOnClickListener{
            finish()
        }
        populateCibi(data,MyAppGlobals.globalVariableEmail)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Recap Giornaliero")
        val email=MyAppGlobals.globalVariableEmail
        val allenamentiSvolti=db.collection("Allenamento",).whereEqualTo("dataAll",data ).whereEqualTo("email",email)
        allenamentiSvolti.get().addOnSuccessListener {
                querySnapshot ->
            if (querySnapshot.isEmpty) {
                all.text = "Non ti sei allenato questo giorno"
                return@addOnSuccessListener
            }else {
                var nScheda=0
                var idSch=""
                for (document in querySnapshot.documents) {
                     idSch += document.getString("idScheda")
                }
                nScheda = when (idSch) {
                    "idScheda1" -> 1
                    "idScheda2" -> 2
                    else -> 3
                }
                all.text="Hai svolto la scheda num:$nScheda"
            }
        }
    }
    private fun populateCibi( date:String,gmail:String){
        val tutto=db.collection("Allenamento",).whereEqualTo("dataAll",data ).whereEqualTo("email",gmail)
        tutto.get().addOnSuccessListener { result ->
            val nomeCibi = ArrayList<String>()
            val infoCibi = HashMap<String, Map<String, String>>()
            for (document in result) {
                val nomeCibo = document.getString("nomeEsercizio")
                val mom=document.getString("periodoGiornata")
                val weight=document.getString("peso")
                if (nomeCibo != null && mom!= null && weight !=null ) {
                    nomeCibi.add(nomeCibo)
                    val exerciseInfo = mapOf(
                        "peso" to weight,
                        "periodoGiornata" to mom
                    )
                    infoCibi[nomeCibo] = exerciseInfo
                }else{
                    info.text="Non hai mangiato nulla durante questo giorno"
                }
            }
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
                ){
                    selectedExercise = parent.getItemAtPosition(position).toString()
                    val exerciseInfo = infoCibi[selectedExercise]
                    if (exerciseInfo != null) {
                        val periodo = exerciseInfo["periodoGiornata"]
                        val weight = exerciseInfo["peso"]
                        val stringaCompleta="Periodo:$periodo /N Peso di:$weight"
                        info.text=stringaCompleta
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //Se non è stato selezionato nulla non fare nulla
                }
            }
        }
    }
}
