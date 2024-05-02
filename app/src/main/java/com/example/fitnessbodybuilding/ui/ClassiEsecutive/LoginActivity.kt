package com.example.fitnessbodybuilding.ui.ClassiEsecutive

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnessbodybuilding.R

class LoginActivity : AppCompatActivity() {
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btn=findViewById<Button>(R.id.btnLogin)
        val noAcc=findViewById<TextView>(R.id.noAccount)
        btn.setOnClickListener{onClick(it)}
        noAcc.setOnClickListener{onClick(it)}
        }

    fun onClick(v: View){
        if(v.id==R.id.btnLogin)
        {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }else if(v.id==R.id.noAccount){
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }

    }