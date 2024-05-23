package com.example.fitnessbodybuilding.ui.ClassiEsecutive

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnessbodybuilding.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email:String
    private lateinit var pass:String
    private var emailCorr: Boolean=false
    private var passCorr: Boolean=false

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        val btn=findViewById<Button>(R.id.btnLogin)
        val noAcc=findViewById<TextView>(R.id.noAccount)
        btn.setOnClickListener{onClick(it)}
        noAcc.setOnClickListener{onClick(it)}
        }

    fun onClick(v: View){
        if(v.id==R.id.btnLogin)
        {
            email=findViewById<TextView>(R.id.et_email).text.toString()
            pass=findViewById<TextView>(R.id.et_pass).text.toString()
            if(email.isEmpty())
            {
                findViewById<TextView>(R.id.emailText).setError("Il campo non può essere vuoto")
            }else if(email.contains("@"))
            {
                emailCorr=true
            }else{
                findViewById<TextView>(R.id.et_email).setError("Inserire formato corretto di email")

            }
            if(pass.isEmpty())
            {
                findViewById<TextView>(R.id.et_pass).setError("Inserire formato corretto di email")
            }else{passCorr=true}
            if((emailCorr)&&(passCorr))
            {
                signIn(email, pass)
            }
        }else if(v.id==R.id.noAccount){
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Login Non Riuscito", Toast.LENGTH_SHORT).show()

                }
            }
    }

    }