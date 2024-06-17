package com.example.fitnessbodybuilding.ui.schedaEs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.fitnessbodybuilding.R

class schedaEsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scheda, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button3 = view.findViewById<Button>(R.id.button3)
        val button4 = view.findViewById<Button>(R.id.button4)
        val button8 = view.findViewById<Button>(R.id.button8)


        button3.setOnClickListener { openEserciziActivity("scheda1") }
        button4.setOnClickListener { openEserciziActivity("scheda2") }
        button8.setOnClickListener { openEserciziActivity("scheda3") }
    }

    private fun openEserciziActivity(schedaId: String) {
        val intent = Intent(activity, EserciziActivity::class.java)
        intent.putExtra("SCHEDE_ID", schedaId)
        startActivity(intent)
    }
}

