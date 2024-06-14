package com.example.fitnessbodybuilding.ui.esercizi



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.fitnessbodybuilding.R
import com.google.firebase.firestore.FirebaseFirestore

class EserciziFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var exerciseSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise, container, false)

        db = FirebaseFirestore.getInstance()
        exerciseSpinner = view.findViewById(R.id.exerciseSpinner)

        // Recupera i dati da Firestore e popola la lista di selezione
        populateExerciseSpinner()

        return view
    }

    private fun populateExerciseSpinner() {
        db.collection("Esercizi")
            .get()
            .addOnSuccessListener { result ->
                val exerciseNames = ArrayList<String>()
                for (document in result) {
                    val exerciseName = document.getString("nomeEsercizio")
                    if (exerciseName != null) {
                        exerciseNames.add(exerciseName)
                    }
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    exerciseNames
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                exerciseSpinner.adapter = adapter

            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Errore nel recupero degli esercizi", exception)
            }
    }

    companion object {
        private const val TAG = "EserciziFragment"
    }
}
