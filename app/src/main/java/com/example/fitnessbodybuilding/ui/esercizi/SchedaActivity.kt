package com.example.fitnessbodybuilding.ui.esercizi



import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnessbodybuilding.R
import com.example.fitnessbodybuilding.ui.ClassiEsecutive.MyAppGlobals
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SchedaActivity : AppCompatActivity() {
    private lateinit var idScheda: String
    private lateinit var schede: String
    private lateinit var exercise: String
    private var documentCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dati_scheda)

        val scheda = intent.getStringExtra("SCHEDA") ?: "Scheda non trovata"
        val esercizio = intent.getStringExtra("ESERCIZIO") ?: "Esercizio non trovato"

        val textView13: TextView = findViewById(R.id.textView13)
        val textView14: TextView = findViewById(R.id.textView14)
        val inserisci: Button = findViewById(R.id.button2)

        textView13.text = scheda
        schede = scheda
        textView14.text = esercizio
        exercise = esercizio

        inserisci.setOnClickListener { onClick(it) }
    }

    private fun onClick(v: View) {
        val rep = findViewById<EditText>(R.id.editTextNumber).text.toString()
        val serie = findViewById<EditText>(R.id.editTextNumber2).text.toString()
        val recupero = findViewById<EditText>(R.id.editTextTime).text.toString()

        if (v.id == R.id.button2) {
            if (schede == "Scheda 1") {
                idScheda = "idScheda1"
            } else if (schede == "Scheda 2") {
                idScheda = "idScheda2"
            } else if (schede == "Scheda 3") {
                idScheda = "idScheda3"
            }

            var ripetizioni = true
            var series = true
            var tempo = true

            if (rep.isEmpty()) {
                findViewById<EditText>(R.id.editTextNumber).error = "Il campo non può essere vuoto"
                ripetizioni = false
            }
            if (serie.isEmpty()) {
                findViewById<EditText>(R.id.editTextNumber2).error = "Il campo non può essere vuoto"
                series = false
            }
            if (recupero.isEmpty()) {
                findViewById<EditText>(R.id.editTextTime).error = "Il campo non può essere vuoto"
                tempo = false
            }

            val gmail = MyAppGlobals.globalVariableEmail
            if (ripetizioni && series && tempo) {
                val user = hashMapOf(
                    "email" to gmail,
                    "idScheda" to idScheda,
                    "nRipetizioni" to "$rep x $serie",
                    "nomeEsercizio" to exercise,
                    "riposoSerie" to recupero
                )
                val db = Firebase.firestore
                db.collection("Utente").get().addOnSuccessListener { querySnapshot ->
                    documentCount = querySnapshot.size()
                    val nesercizi = documentCount
                    db.collection("SchedaAllenamento").document("esercizioScheda$nesercizi").set(user)
                        .addOnSuccessListener {
                            // Mostra pop-up di conferma
                            val builder = AlertDialog.Builder(this)
                            builder.setMessage("Dati aggiunti")
                                .setPositiveButton("OK") { _, _ ->
                                    // Torna al fragment
                                    finish()
                                }
                            builder.create().show()
                        }
                        .addOnFailureListener { exception ->
                            // Gestire eventuali errori qui
                            val builder = AlertDialog.Builder(this)
                            builder.setMessage("Errore nell'inserimento dei dati: ${exception.message}")
                                .setPositiveButton("OK", null)
                            builder.create().show()
                        }
                }.addOnFailureListener { exception ->
                    // Gestire eventuali errori qui
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Errore nel recupero dei dati: ${exception.message}")
                        .setPositiveButton("OK", null)
                    builder.create().show()
                }
            }
        }
    }
}
