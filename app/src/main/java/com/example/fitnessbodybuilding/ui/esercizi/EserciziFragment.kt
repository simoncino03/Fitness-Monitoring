package com.example.fitnessbodybuilding.ui.esercizi



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.fitnessbodybuilding.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EserciziFragment : Fragment() {

    private lateinit var exerciseSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exercise, container, false)

        exerciseSpinner = view.findViewById(R.id.exerciseSpinner)


        loadExercises()

        return view
    }

    private fun loadExercises() {
        val db = Firebase.firestore
        db.collection("Esercizi").get()
            .addOnSuccessListener { result ->
                val exercises = mutableListOf<String>()

                for (document in result) {
                    val nomeEsercizio = document.getString("nomeEsercizi")
                    Log.d(TAG, "Exercise name: $nomeEsercizio")
                    nomeEsercizio?.let { exercises.add(it) }
                }

                if (exercises.isEmpty()) {
                    Log.d(TAG, "No exercises found")
                }

                context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item,
                        exercises
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        exerciseSpinner.adapter = adapter
                    }
                }

                exerciseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        // Qui puoi gestire cosa succede quando un esercizio viene selezionato
                        val selectedExercise = exercises[position]
                        Log.d(TAG, "Selected: $selectedExercise")
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Gestione caso in cui non è stato selezionato nulla
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    companion object {
        private const val TAG = "ExerciseFragment"
    }
}
