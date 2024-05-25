package com.example.fitnessbodybuilding.ui.schedaEs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessbodybuilding.R
import com.example.fitnessbodybuilding.databinding.FragmentSchedaBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class schedaEsFragment : Fragment() {

    private var _binding: FragmentSchedaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scheda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ottenere una istanza del database Firestore
        val db = Firebase.firestore

        // Riferimento al documento "Scheda" nella collezione "fitness"
        val docRef = db.collection("fitness").document("Scheda")

        // Estrarre i dati dal documento
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val numEs = document.getLong("numEs") ?: 0
                    val immagine = document.getString("immagine") ?: ""

                    // Mostrare i dati a schermo (ad esempio nei Log)
                    Log.d("FirestoreData", "Numero di esercizi: $numEs")
                    Log.d("FirestoreData", "Immagine URL: $immagine")

                    // Aggiornare l'UI con i dati estratti, ad esempio:
                    // textViewNumeroEsercizi.text = "Numero di esercizi: $numEs"
                    // Picasso.get().load(immagine).into(imageViewImmagine)
                } else {
                    Log.d("FirestoreData", "Il documento non esiste.")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("FirestoreData", "Errore: ", exception)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}