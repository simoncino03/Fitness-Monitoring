package com.example.fitnessbodybuilding.ui.esercizi
import android.app.AlertDialog
import android.content.Intent
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
import android.widget.Button
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
    private lateinit var showDialogButton: Button
    private var selectedExercise: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise, container, false)

        db = FirebaseFirestore.getInstance()
        exerciseSpinner = view.findViewById(R.id.exerciseSpinner)//Spinner che serve per creare il menu a tendina
        exerciseImageView = view.findViewById(R.id.exerciseImage)
        muscle = view.findViewById(R.id.muscleGroup)
        showDialogButton = view.findViewById(R.id.button)//Bottone "aggiungi alla scheda"

        populateExerciseSpinner()//Con questo metodo viene popolato il menu degli esercizi
        showDialogButton.setOnClickListener {
            showDialog()
        }
        return view
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.layout_inserimento_scheda, null)
        builder.setView(dialogLayout)

        val button1: Button = dialogLayout.findViewById(R.id.button5)
        val button2: Button = dialogLayout.findViewById(R.id.button6)
        val button3: Button = dialogLayout.findViewById(R.id.button7)

        val dialog = builder.create()

        val clickListener = View.OnClickListener { v ->
            val scheda = when (v.id) {
                R.id.button5 -> "Scheda 1"
                R.id.button6 -> "Scheda 2"
                R.id.button7 -> "Scheda 3"
                else -> ""
            }
            val intent = Intent(requireContext(), SchedaActivity::class.java).apply {
                putExtra("SCHEDA", scheda)
                putExtra("ESERCIZIO", selectedExercise)
            }
            startActivity(intent)
            dialog.dismiss()
        }

        button1.setOnClickListener(clickListener)
        button2.setOnClickListener(clickListener)
        button3.setOnClickListener(clickListener)

        dialog.show()
    }

    private fun populateExerciseSpinner() {
        db.collection("Esercizi")
            .get()
            .addOnSuccessListener { result ->
                val exerciseNames = ArrayList<String>()
                val exerciseData = HashMap<String, Map<String, String>>()

                for (document in result) {
                    val exerciseName = document.getString("nomeEsercizio")
                    val exerciseImageUrl = document.getString("imageURL")
                    val muscleGroup = document.getString("gruppoMusc")
                    if (exerciseName != null && exerciseImageUrl != null && muscleGroup != null) {
                        exerciseNames.add(exerciseName)
                        val exerciseInfo = mapOf(
                            "imageURL" to exerciseImageUrl,
                            "gruppoMusc" to muscleGroup
                        )
                        exerciseData[exerciseName] = exerciseInfo//Nella posizione corrispondente a exerciseName,aggiungiamo i suoi "attributi"
                    }
                }
                //Viene creato un adapter per gestire lo spinner
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    exerciseNames
                )
                //l'istruzione seguente serve per creare il layout del singolo spinner,interna ad android
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                exerciseSpinner.adapter = adapter

                exerciseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        selectedExercise = parent.getItemAtPosition(position).toString()//Estrae il nome dell'esercizio selezionato
                        val exerciseInfo = exerciseData[selectedExercise]//in exerciseInfo saranno presenti l'immagine e i muscoli interessati dall'esercizio
                        if (exerciseInfo != null) {
                            val imageUrl = exerciseInfo["imageURL"]
                            val muscleGroup = exerciseInfo["gruppoMusc"]
                            muscle.text = muscleGroup
                            if (imageUrl != null) {
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
                connection.doInput = true//Indichiamo che useremo la connessione per mandare e riceveere dagti
                connection.connect()//Si connette all'url
                val input: InputStream = connection.inputStream//Salva in input l'immagine ricevuta
                bitmap = BitmapFactory.decodeStream(input)//Questa riga di codice ha il compito din tradurre lo stream ricevuto in un bitmap
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
