package com.example.fitnessbodybuilding.ui.esercizi

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitnessbodybuilding.R
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class EserciziFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var exerciseSpinner: Spinner
    private lateinit var exerciseImageView: ImageView
    private lateinit var muscle: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise, container, false)

        db = FirebaseFirestore.getInstance()
        exerciseSpinner = view.findViewById(R.id.exerciseSpinner)
        exerciseImageView = view.findViewById(R.id.exerciseImage)
        muscle = view.findViewById(R.id.muscleGroup) // Riferimento al TextView per il gruppo muscolare

        // Recupera i dati da Firestore e popola la lista di selezione
        populateExerciseSpinner()

        return view
    }

    private fun populateExerciseSpinner() {
        db.collection("Esercizi")
            .get()
            .addOnSuccessListener { result ->
                val exerciseNames = ArrayList<String>()
                val exerciseData = HashMap<String, Map<String, String>>() // Modifica il tipo di dati

                for (document in result) {
                    val exerciseName = document.getString("nomeEsercizio")
                    val exerciseImageUrl = document.getString("imageURL")
                    val muscleGroup = document.getString("gruppoMusc") // Aggiunge il campo gruppo muscolare
                    if (exerciseName != null && exerciseImageUrl != null && muscleGroup != null) {
                        exerciseNames.add(exerciseName)
                        val exerciseInfo = mapOf(
                            "imageURL" to exerciseImageUrl,
                            "gruppoMusc" to muscleGroup
                        )
                        exerciseData[exerciseName] = exerciseInfo
                    }
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    exerciseNames
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                exerciseSpinner.adapter = adapter

                exerciseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val selectedExercise = parent.getItemAtPosition(position).toString()
                        val exerciseInfo = exerciseData[selectedExercise]
                        if (exerciseInfo != null) {
                            val imageUrl = exerciseInfo["imageURL"]
                            val muscleGroup = exerciseInfo["gruppoMusc"]
                            muscle.text = muscleGroup // Imposta il testo nel TextView per il gruppo muscolare
                            if (imageUrl != null) {
                                // Carica l'immagine utilizzando AsyncTask
                                DownloadImageTask(exerciseImageView).execute(imageUrl)
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // Non fare nulla
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Errore nel recupero degli esercizi", exception)
            }
    }

    // AsyncTask per il caricamento dell'immagine
    private class DownloadImageTask(private val imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String?): Bitmap? {
            val imageUrl = urls[0]
            var bitmap: Bitmap? = null
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                bitmap = BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            result?.let {
                imageView.setImageBitmap(it)
            }
        }
    }

    companion object {
        private const val TAG = "EserciziFragment"
    }
}

