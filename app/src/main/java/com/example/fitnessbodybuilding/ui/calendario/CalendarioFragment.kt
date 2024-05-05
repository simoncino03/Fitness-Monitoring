package com.example.fitnessbodybuilding.ui.calendario

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            sfondoTot=view.findViewById<LinearLayout>(R.id.sfondo)
            layout = view.findViewById(R.id.cal)
            text = view.findViewById(R.id.testo)
            cal = view.findViewById(R.id.calend)
           /* cal.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
                val intent = Intent(requireContext(), PaginaGiornaliera::class.java)
                startActivity(intent)
            }*/
            return view
        }
    }
}