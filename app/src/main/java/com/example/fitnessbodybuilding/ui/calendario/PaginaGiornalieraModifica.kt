package com.example.fitnessbodybuilding.ui.calendario

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitnessbodybuilding.R
import com.example.fitnessbodybuilding.ui.ClassiEsecutive.MyAppGlobals
import com.example.fitnessbodybuilding.ui.esercizi.SchedaActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class PaginaGiornalieraModifica : AppCompatActivity() {
    private  var dataSelezionata:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_giornaliera_modifica)
        dataSelezionata=intent.getStringExtra("Giorno")
        findViewById<TextView>(R.id.giornoSelez).text=dataSelezionata
        val btnAllenamento=findViewById<Button>(R.id.btnAll)
        val btnAlimentazione=findViewById<Button>(R.id.btnAli)
        val btnIndietro=findViewById<Button>(R.id.btnInd)
        btnAllenamento.setOnClickListener{azioneBottoni(it)}
        btnAlimentazione.setOnClickListener { azioneBottoni(it) }
        btnIndietro.setOnClickListener { azioneBottoni(it) }

    }

    private fun azioneBottoni(v:View) {
        if(v.id==R.id.btnAll)
        {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.layout_inserimento_scheda, null)
            builder.setView(dialogLayout)
            val nomePopUpTextView: TextView = dialogLayout.findViewById(R.id.nomePopUp)
            nomePopUpTextView.text = "Che scheda hai utilizzato?"
            val button1: Button = dialogLayout.findViewById(R.id.button5)
            val button2: Button = dialogLayout.findViewById(R.id.button6)
            val button3: Button = dialogLayout.findViewById(R.id.button7)

            val dialog = builder.create()

            val clickListener = View.OnClickListener { v ->
                val scheda = when (v.id) {
                    R.id.button5 -> "1"
                    R.id.button6 -> "2"
                    R.id.button7 -> "3"
                    else -> ""
                }
                val email=MyAppGlobals.getGlobalVariableEmail()
                val allenamentoEff = hashMapOf(
                    "dataAll" to dataSelezionata,
                    "email" to email,
                    "idScheda" to "idScheda$scheda"
                )

                val db = Firebase.firestore
                db.collection("Allenamento").get().addOnSuccessListener { querySnapshot ->
                    val documentCount = querySnapshot.size()
                    val nUtenti = documentCount
                    db.collection("Allenamento").document("allenamento$nUtenti").set(allenamentoEff)
                }

                val confirmDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
                confirmDialogBuilder.setMessage("Dati aggiunti")
                    .setPositiveButton("OK") { _, _ ->
                        val intent = Intent(this, PaginaGiornalieraModifica::class.java)
                        startActivity(intent)
                    }
                confirmDialogBuilder.create().show()
            }
            button1.setOnClickListener(clickListener)
            button2.setOnClickListener(clickListener)
            button3.setOnClickListener(clickListener)
            dialog.show()
        }else if(v.id==R.id.btnAli){
            setContentView(R.layout.informazioni_cibo)
            val inserisci: Button = findViewById(R.id.inserisci)
            inserisci.setOnClickListener{aggiuntaCibi(it)}

        }else if(v.id==R.id.btnInd)
        {
            finish();
            //val intent = Intent(this, CalendarioFragment::class.java)
            //startActivity(intent)
        }
    }

    private fun aggiuntaCibi(v:View) {
        val nameFood: String = findViewById<TextView?>(R.id.nomeCibo).text.toString()
        val momDay: String = findViewById<TextView?>(R.id.momento).text.toString()
        val kilog: String = findViewById<TextView?>(R.id.quantita).text.toString()

        var nomeCorr = true
        var momCorr = true
        var kilogCorr = true

        if (nameFood.isEmpty()) {
            findViewById<EditText>(R.id.editTextNumber).error = "Il campo non può essere vuoto"
            nomeCorr = false
        }
        if (momDay.isEmpty()) {
            findViewById<EditText>(R.id.editTextNumber2).error = "Il campo non può essere vuoto"
            momCorr = false
        }
        if (kilog.isEmpty()) {
            findViewById<EditText>(R.id.editTextTime).error = "Il campo non può essere vuoto"
            kilogCorr = false
        }
        if(nomeCorr && momCorr &&kilogCorr)
        {
            val alimento= hashMapOf(
                "dataMangiato" to dataSelezionata,
                "email" to MyAppGlobals.getGlobalVariableEmail(),
                "nomeAlimento" to nameFood,
                "periodoGiornata" to momDay,
                "peso" to kilog)

                val db=Firebase.firestore
                db.collection("Alimentazione").get().addOnSuccessListener { querySnapshot ->
                    val documentCount = querySnapshot.size()
                    val newDocumentId = "alimento${documentCount + 1}"
                    db.collection("Alimentazione").document(newDocumentId).set(alimento)
                        .addOnSuccessListener {
                            // Mostra pop-up di conferma
                            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                            builder.setMessage("Dati Inseriti")
                                .setPositiveButton("OK") { _, _ ->
                                    val intent = Intent(this, PaginaGiornalieraModifica::class.java)
                                    startActivity(intent)
                                }
                            builder.create().show()
                        }
                }
        }
    }
}
