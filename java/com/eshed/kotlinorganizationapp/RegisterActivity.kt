package com.eshed.kotlinorganizationapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class RegisterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            Log.d("MainActivity","Password is " + password)
            Log.d("MainActivity","Username is " + username)
        }

        registerAccountTextView.setOnClickListener {
            Log.d("MainActivity","try to show log in activity")

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}