package com.example.fitnessbodybuilding.ui.calendario

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitnessbodybuilding.R
import com.example.fitnessbodybuilding.ui.ClassiEsecutive.MenuActivity

class CalendarioFragment : Fragment() {

    private lateinit var layout: LinearLayout
    private lateinit var text: TextView
    private lateinit var cal: CalendarView
    private lateinit var sfondoTot:LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendario, container, false)
        sfondoTot=view.findViewById(R.id.sfondo)
        layout = view.findViewById(R.id.cal)
        text = view.findViewById(R.id.testo)
        cal = view.findViewById(R.id.calend)
        cal.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Azione da eseguire quando la data viene modificata
            // In questo esempio, l'azione è vuota
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.popup_layout)

            val buttonOption1 = dialog.findViewById<Button>(R.id.buttonOption1)
            buttonOption1.setOnClickListener {
                // Gestione clic su Option 1
            }

            val buttonOption2 = dialog.findViewById<Button>(R.id.buttonOption2)
            buttonOption2.setOnClickListener {
                // Gestione clic su Option 2
            }

            dialog.show()
        }
        return view
    }
}