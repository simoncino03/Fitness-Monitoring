package com.example.fitnessbodybuilding.ui.schedaEs

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
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.fitnessbodybuilding.R
import com.example.fitnessbodybuilding.databinding.FragmentSchedaBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayDeque
import java.util.Locale

class schedaEsFragment : Fragment() {
    private lateinit var listView : ListView
    private lateinit var list: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var reference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scheda, container, false)
        listView= view.findViewById(R.id.textViewNumeroEsercizi)
        adapter= ArrayAdapter(requireContext(), android.R.layout.test_list_item)
        reference= FirebaseDatabase.getInstance().getReference().child("Scheda")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (snapshot in dataSnapshot.children) {
                    list.add(snapshot.getValue(String::class.java).toString())
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Il tuo codice quando c'è un errore
            }
        })
        return view
        }

}