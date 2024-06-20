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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var pass: String
    private var emailCorr: Boolean = false
    private var passCorr: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        val btn = findViewById<Button>(R.id.btnLogin)
        val noAcc = findViewById<TextView>(R.id.noAccount)
        btn.setOnClickListener { onClick(it) }
        noAcc.setOnClickListener { onClick(it) }
    }

    fun onClick(v: View) {
        if (v.id == R.id.btnLogin) {
            email = findViewById<TextView>(R.id.et_email).text.toString()
            pass = findViewById<TextView>(R.id.et_pass).text.toString()
            if (email.isEmpty()) {
                findViewById<TextView>(R.id.et_email).setError("Il campo non può essere vuoto")
            } else if (email.contains("@")) {
                emailCorr = true
            } else {
                findViewById<TextView>(R.id.et_email).setError("Inserire formato corretto di email")

            }
            if (pass.isEmpty()) {
                findViewById<TextView>(R.id.et_pass).setError("Il campo non può essere vuoto")
            } else {
                passCorr = true
            }
            if ((emailCorr) && (passCorr)) {
                signIn(email, pass)
            }
        } else if (v.id == R.id.noAccount) {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }else if(v.id==R.id.pass_dimenticata)
        {
            if(emailCorr)
            {
                auth.sendPasswordResetEmail(email).addOnCompleteListener {
                    task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Email per reimpostare la password inviata.", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Errore nell'invio dell'email.", Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                Toast.makeText(this, "Inserisci un formato di email valido grazie", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MenuActivity::class.java)
                    MyAppGlobals.setGlobalVariableEmail(email)
                    startActivity(intent)
                } else {
                    try {
                        throw task.exception ?: Exception("Unknown exception")
                    } catch (e: FirebaseAuthInvalidUserException) {
                        Toast.makeText(
                            this,
                            "Utente non trovato. Verifica l'email inserita.",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Password errata. Riprova.", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Accesso fallito: ${e.message}", Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }
    }
}

object MyAppGlobals {
    private var globalVariableEmail: String = ""
    fun getGlobalVariableEmail(): String {
        return globalVariableEmail
    }

    fun setGlobalVariableEmail(email: String) {
        // Aggiungi qui la logica per validare o controllare la modifica, se necessario
        globalVariableEmail = email
    }
}