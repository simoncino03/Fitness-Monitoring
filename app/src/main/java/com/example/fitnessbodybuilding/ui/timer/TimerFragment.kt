package com.example.fitnessbodybuilding.ui.timer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitnessbodybuilding.R
import java.util.*

class TimerFragment : Fragment() {
    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button
    private lateinit var partialButton: Button
    private lateinit var partialTimesListView: ListView
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 30000 // Tempo predefinito: 30 secondi
    private var timerRunning = false
    private val partialTimesQueue = ArrayDeque<String>()
    private lateinit var partialTimesAdapter: ArrayAdapter<String>
    private var lastSelectedTimeInMillis: Long = 30000 // Tempo predefinito per il reset: 30 secondi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        timerTextView = view.findViewById(R.id.timerTextView)
        startButton = view.findViewById(R.id.startButton)
        stopButton = view.findViewById(R.id.stopButton)
        resetButton = view.findViewById(R.id.resetButton)
        partialButton = view.findViewById(R.id.partialButton)
        partialTimesListView = view.findViewById(R.id.partialTimesListView)

        startButton.setOnClickListener { startTimer() }
        stopButton.setOnClickListener { stopTimer() }
        resetButton.setOnClickListener { resetTimer() }
        partialButton.setOnClickListener { addPartialTime() }

        partialTimesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        partialTimesListView.adapter = partialTimesAdapter

        // Aggiungi il gestore di eventi per modificare il tempo cliccando sul timer
        timerTextView.setOnClickListener {
            showTimePickerDialog()
        }

        return view
    }

    private fun startTimer() {
        //Questo metodo aggiorna il timer a ogni tick
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                //Quando il timer è finito vengono aggiornate tutte le variabili a schermo
                timerRunning = false
                removePartials() // Rimuovi i tempi parziali
                resetTimer() // Resetta il timer automaticamente
            }
        }.start()

        timerRunning = true
        startButton.isEnabled =
            false // Disabilita il pulsante di avvio se il timer è già in esecuzione
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        timerRunning = false
        startButton.isEnabled = true // Assicura che il pulsante di avvio sia abilitato quando il timer viene interrotto
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        timerRunning = false
        timeLeftInMillis = lastSelectedTimeInMillis // Ripristina l'ultimo tempo selezionato
        updateTimer()
        startButton.isEnabled = true
        removePartials() // Rimuovi i tempi parziali quando si preme il pulsante di reset
    }

    private fun updateTimer() {//Metodo che mi permette di vedere sullo schermo il timer
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        //Formatta il timer in base alle impostazioni del telefono tramite il Locale.getDefault
        val timeLeftFormatted =
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        timerTextView.text = timeLeftFormatted

    }

    private fun addPartialTime() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val newPartialTime = String.format("%02d:%02d", minutes, seconds)
        // Aggiungi il nuovo tempo parziale alla coda
        partialTimesQueue.offer(newPartialTime)

        // Se la coda supera la dimensione massima di 3, rimuovi il tempo parziale più vecchio
        if (partialTimesQueue.size > 3) {
            partialTimesQueue.poll()
        }
        updatePartialList()
    }
    private fun removePartials() {
        // Rimuovi tutti i parziali dalla lista
        partialTimesQueue.clear()
        // Aggiorna la lista vuota per nascondere i parziali
        updatePartialList()
    }

    private fun updatePartialList() {
        // Svuota l'adapter prima di aggiungere i tempi parziali
        partialTimesAdapter.clear()

        // Inverti l'ordine dei tempi parziali nella coda
        val reversedPartials = ArrayDeque<String>()
        reversedPartials.addAll(partialTimesQueue.reversed())

        // Aggiungi i tempi parziali dalla coda all'adapter
        reversedPartials.forEach { partialTimesAdapter.add(it) }//Visto che la variabile adapter è collegata alla listView
                                                                //questa istruzione aggiunge il tempo alla coda della listView

        //Notifica android che la lista è cambiata e quindi deve aggiornare la listview
        partialTimesAdapter.notifyDataSetChanged()
    }

    @SuppressLint("MissingInflatedId")
    private fun showTimePickerDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_time_picker, null)
        val minutePicker = view.findViewById<NumberPicker>(R.id.minutePicker)
        val secondPicker = view.findViewById<NumberPicker>(R.id.secondPicker)

        minutePicker.minValue = 0
        minutePicker.maxValue   = 59
        minutePicker.value = (lastSelectedTimeInMillis / 1000 / 60).toInt()

        secondPicker.minValue = 0
        secondPicker.maxValue = 59
        secondPicker.value = ((lastSelectedTimeInMillis / 1000) % 60).toInt()

        dialog.setView(view)
        dialog.setTitle("Set Time")

        dialog.setPositiveButton("Set") { _, _ ->
            val minutes = minutePicker.value
            val seconds = secondPicker.value
            lastSelectedTimeInMillis = (minutes * 60 + seconds) * 1000L
            timeLeftInMillis = lastSelectedTimeInMillis
            updateTimer()
        }
        dialog.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialog.show()
    }
}