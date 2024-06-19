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
import com.example.fitnessbodybuilding.ui.ClassiEsecutive.MyAppGlobals

class  CalendarioFragment : Fragment() {

    private lateinit var layout: LinearLayout
    private lateinit var text: TextView
    private lateinit var cal: CalendarView
    private lateinit var sfondoTot:LinearLayout
    private lateinit var data:String
    private var mese=0
    private lateinit var meseCorr:String
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
             mese=month+1
             if(mese<10)
             {
                 meseCorr="0$mese"
             }else{
                 meseCorr="$mese"
             }
             data="$dayOfMonth-$meseCorr-$year"
            // Azione da eseguire quando la data viene modificata
            // In questo esempio, l'azione è vuota
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.popup_layout)

            val buttonOption1 = dialog.findViewById<Button>(R.id.modData)
            buttonOption1.setOnClickListener {
                // Gestione clic su Option 1
            }

            val buttonOption2 = dialog.findViewById<Button>(R.id.viewData)
            buttonOption2.setOnClickListener {
                val intent = Intent(requireContext(), PaginaGiornalieraVisione::class.java)
                intent.putExtra("Giorno",data)
                startActivity(intent)
            }
            dialog.show()
        }
        return view
    }
}