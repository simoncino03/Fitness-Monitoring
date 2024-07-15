package com.example.fitnessbodybuilding.ui.schedaEs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.fitnessbodybuilding.R
import com.example.fitnessbodybuilding.ui.ClassiEsecutive.MyAppGlobals
import com.google.firebase.firestore.FirebaseFirestore

class EserciziActivity : AppCompatActivity() {

    private lateinit var textViewInfo: TextView
    private lateinit var buttonDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esercizi)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // Imposta la Toolbar come Action Bar
        setSupportActionBar(toolbar)

        textViewInfo = findViewById(R.id.textViewInfo)
        buttonDelete = findViewById(R.id.buttonDelete)

        // Recupera l'ID della scheda dall'intent
        var schedaId = intent.getStringExtra("SCHEDE_ID")

        supportActionBar?.setTitle("$schedaId")

        if (schedaId == "scheda2") {
            schedaId= "idScheda2"
        } else if (schedaId == "scheda1") {
            schedaId = "idScheda1"
        } else if (schedaId == "scheda3") {
            schedaId = "idScheda3"
        }
        //Si può fare anche cosi:
        //private fun convertSchedaId(schedaId: String?): String? {
        //    return when (schedaId) {
        //        "scheda1" -> "idScheda1"
        //        "scheda2" -> "idScheda2"
        //        "scheda3" -> "idScheda3"
        //        else -> null
        //    }
        //}

        // Recupera l'email dall'applicazione globale
        val gmail = MyAppGlobals.getGlobalVariableEmail()
        if (gmail == null) {
            // Gestisci l'errore: Email globale non presente
            finish()
            return
        }

        // Configura Firebase
        val db = FirebaseFirestore.getInstance()
        //Questa bvariabie contine una query che filtra i risultati nella collezione "SchedaAllenamento"
        val eserciziRef = db.collection("SchedaAllenamento")
            .whereEqualTo("idScheda", schedaId)

        // Esegui la query per recuperare gli esercizi associati alla scheda specificata
        eserciziRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Nessun esercizio trovato per questa scheda
                    textViewInfo.text = "Nessun esercizio trovato per la scheda con ID: $schedaId"
                    return@addOnSuccessListener
                }
                // Costruisci il testo per visualizzare gli esercizi
                val stringBuilder = StringBuilder()
                for (document in querySnapshot.documents) {
                    val nRipetizioni = document.getString("nRipetizioni") ?: "N/D"
                    val nomeEsercizio = document.getString("nomeEsercizio") ?: "N/D"
                    val riposoSerie = document.getString("riposoSerie") ?: "N/D"

                    stringBuilder.append("Esercizio: $nomeEsercizio\n")
                    stringBuilder.append("Ripetizioni: $nRipetizioni\n")
                    stringBuilder.append("Riposo tra serie: $riposoSerie\n\n")
                }

                // Mostra il testo nella TextView
                textViewInfo.text = stringBuilder.toString()
            }
            .addOnFailureListener { exception ->
                // Gestisci l'errore durante il recupero degli esercizi
                Log.e("EserciziActivity", "Errore durante il recupero degli esercizi", exception)
                textViewInfo.text = "Errore durante il recupero degli esercizi"
            }

        // Imposta il listener per il bottone di eliminazione
        buttonDelete.setOnClickListener {
            if (schedaId != null) {
                deleteScheda(db, schedaId)
            }
        }
    }

    private fun deleteScheda(db: FirebaseFirestore, schedaId: String) {
        val schedaRef = db.collection("SchedaAllenamento")
            .whereEqualTo("idScheda", schedaId)

        // Trova il documento da eliminare
        schedaRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    document.reference.delete()
                        .addOnSuccessListener {
                            textViewInfo.text = "Scheda eliminata con successo"
                        }
                        .addOnFailureListener { e ->
                            Log.e("EserciziActivity", "Errore durante l'eliminazione della scheda", e)
                            textViewInfo.text = "Errore durante l'eliminazione della scheda"
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("EserciziActivity", "Errore durante la ricerca della scheda", e)
                textViewInfo.text = "Errore durante la ricerca della scheda"
            }
    }
}
