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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fitnessbodybuilding.R
import com.google.firebase.firestore.FirebaseFirestore

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
        val exerciseData = hashMapOf(
            "immagine" to imageRes,
            "numEs" to exercise
        )

        // Aggiungi i dati al documento "esercizi" nella raccolta "Fitness"
        firestore.collection("Fitness")
            .document("Scheda")
            .set(exerciseData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Esercizio aggiunto con successo", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Errore durante l'aggiunta dell'esercizio", Toast.LENGTH_SHORT).show()
                }
            }
    }
}