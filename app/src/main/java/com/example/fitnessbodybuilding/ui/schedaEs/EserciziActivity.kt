package com.example.fitnessbodybuilding.ui.schedaEs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.fitnessbodybuilding.R
import com.example.fitnessbodybuilding.ui.ClassiEsecutive.MyAppGlobals
import com.example.fitnessbodybuilding.ui.calendario.CalendarioFragment
import com.example.fitnessbodybuilding.ui.calendario.PaginaGiornalieraVisione
import com.google.firebase.firestore.FirebaseFirestore

class EserciziActivity : AppCompatActivity() {

    private lateinit var textViewInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esercizi)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // Imposta la Toolbar come Action Bar
        setSupportActionBar(toolbar)

        textViewInfo = findViewById(R.id.textViewInfo)

        // Recupera l'ID della scheda dall'intent
        var schedaId = intent.getStringExtra("SCHEDE_ID")

        supportActionBar?.setTitle("$schedaId")

        if (schedaId == "scheda2") {
            schedaId= "idScheda2"
        }else if(schedaId=="scheda1"){
            schedaId="idScheda1"
        }else if(schedaId=="scheda3"){
            schedaId="idScheda3"
        }

        // Recupera l'email dall'applicazione globale
        val gmail = MyAppGlobals.getGlobalVariableEmail()
        if (gmail == null) {
            // Gestisci l'errore: Email globale non presente
            finish()
            return
        }

        // Configura Firebase
        val db = FirebaseFirestore.getInstance()
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
    }
}
