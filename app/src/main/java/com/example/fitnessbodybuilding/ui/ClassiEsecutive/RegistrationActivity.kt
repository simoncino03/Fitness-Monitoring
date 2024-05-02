package com.example.fitnessbodybuilding.ui.ClassiEsecutive

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnessbodybuilding.R
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import java.util.*

class RegistrationActivity : AppCompatActivity() {
    private lateinit var editTextDate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        editTextDate = findViewById(R.id.editTextText3)
        val hoAcc=findViewById<TextView>(R.id.hoAcc)
        val btnReg=findViewById<Button>(R.id.btnReg)
        hoAcc.setOnClickListener{onClick(it)}
        btnReg.setOnClickListener{onClick(it)}
    }

    fun onClick(v:View)
    {
        if(v.id==R.id.hoAcc)
        {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else if(v.id==R.id.btnReg){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    fun showDatePickerDialog(v: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val date = "$dayOfMonth/${month + 1}/$year"
                editTextDate.setText(date)
            }, year, month, day
        )
        datePickerDialog.show()
    }
}