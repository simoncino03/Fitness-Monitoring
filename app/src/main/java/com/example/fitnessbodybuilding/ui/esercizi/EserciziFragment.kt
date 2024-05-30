package com.example.fitnessbodybuilding.ui.esercizi
import android.os.Bundle
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
import com.google.common.base.Objects
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

private fun <K, V> HashMap<K, V>.put(key: String, value: V) {

}

class EserciziFragment : Fragment() {

    private lateinit var exerciseSpinner: Spinner
    private lateinit var exerciseImage: ImageView
    private lateinit var addExerciseButton: Button
    private lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise, container, false)

        exerciseSpinner = view.findViewById(R.id.exerciseSpinner)
        exerciseImage = view.findViewById(R.id.exerciseImage)
        addExerciseButton = view.findViewById(R.id.button)
        firestore = FirebaseFirestore.getInstance()
        // Popoliamo lo Spinner con le opzioni degli esercizi
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.exercise_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            exerciseSpinner.adapter = adapter
        }

        // Impostiamo un listener per lo Spinner per gestire la selezione dell'utente
        exerciseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Quando un elemento è selezionato, mostriamo l'immagine corrispondente
                when (position) {
                    0 -> exerciseImage.setImageResource(R.drawable.exercise_image_1)
                    1 -> exerciseImage.setImageResource(R.drawable.exercise_image_3)
                    2 -> exerciseImage.setImageResource(R.drawable.exercise_image_2)
                    // Aggiungi casi per altri esercizi
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Gestione caso in cui non è stato selezionato nulla
            }
        }

        // Listener per il bottone per aggiungere esercizi a Firestore
        addExerciseButton.setOnClickListener {
            val selectedExercise = exerciseSpinner.selectedItem.toString()
            val selectedImageRes = when (exerciseSpinner.selectedItemPosition) {
                0 -> R.drawable.exercise_image_1
                1 -> R.drawable.exercise_image_3
                2 -> R.drawable.exercise_image_2 // Immagine di default
                else -> {}
            }

            addExerciseToFirestore(selectedExercise, selectedImageRes)
        }

        return view
    }

    private fun addExerciseToFirestore(exercise: String, imageRes: Any) {
        // Crea un mappa con i dati da inserire
        val map = java.util.HashMap<String , Any>()
        map.put("exercise", exercise);
        map.put("imagine", imageRes)

        FirebaseDatabase.getInstance().getReference().child("Scheda").push().child("esercizi").setValue(exercise)
        FirebaseDatabase.getInstance().getReference().child("Scheda").push().child("immagini").setValue(imageRes)
            }
    }
