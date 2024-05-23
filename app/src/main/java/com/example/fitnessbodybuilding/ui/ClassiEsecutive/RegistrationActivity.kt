package com.example.fitnessbodybuilding.ui.ClassiEsecutive


import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnessbodybuilding.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.NetworkOnMainThreadException
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text
import java.util.*


class RegistrationActivity : AppCompatActivity() {
    private lateinit var editTextDate: EditText
    private lateinit var auth:FirebaseAuth
    private lateinit var nome: String
    private lateinit var cognome:String
    private lateinit var emText:TextView
    private lateinit var email:String
    private lateinit var pass:String
    private lateinit var data:String
    private var nomCor:Boolean=false
    private var cognomCor:Boolean=false
    private var emailCorr:Boolean=false
    private var passCorr:Boolean=false
    private var dataCorr:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        editTextDate = findViewById(R.id.editTextText3)
        val hoAcc=findViewById<TextView>(R.id.hoAcc)
        val btnReg=findViewById<Button>(R.id.btnReg)
        auth=FirebaseAuth.getInstance()
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
            nome= findViewById<TextView>(R.id.name).text.toString()
            cognome=findViewById<TextView>(R.id.surname).text.toString()
            emText=findViewById<TextView>(R.id.emailText)
            email=emText.text.toString()
            data=findViewById<EditText>(R.id.editTextText3).text.toString()
            pass=findViewById<TextView>(R.id.passText).text.toString()
                if(nome.isEmpty())
                {
                    findViewById<TextView>(R.id.name).setError("Il campo non può essere vuoto")
                }else
                {
                    nomCor=true

                }
            if(cognome.isEmpty())
            {
                findViewById<TextView>(R.id.surname).setError("Il campo non può essere vuoto")
            }else{                cognomCor=true
            }
            if(email.isEmpty())
            {
                findViewById<TextView>(R.id.emailText).setError("Il campo non può essere vuoto")
            }else if(email.contains("@"))
            {
                emailCorr=true
            }else{
                findViewById<TextView>(R.id.emailText).setError("Inserire formato corretto di email")

            }
            if(pass.isEmpty())
            {
                findViewById<TextView>(R.id.passText).setError("Il campo non può essere vuoto")
                passCorr=true
            }else{                passCorr=true
            }
            if(data.isEmpty())
            {
                findViewById<EditText>(R.id.editTextText3).setError("Il campo non può essere vuoto")
            }else{                dataCorr=true
            }
            if((nomCor)&&(cognomCor)&&(dataCorr)&&(emailCorr)&&(passCorr))
            {
                registerUser(email, pass)
            }
        }
    }


    private fun registerUser(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task:Task<AuthResult> ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Registrazione Fallita", Toast.LENGTH_SHORT).show()
                }
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

