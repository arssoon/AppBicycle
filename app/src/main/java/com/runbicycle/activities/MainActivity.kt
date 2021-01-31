package com.runbicycle.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.runbicycle.R
import kotlinx.android.synthetic.main.activity_menu.*

class MainActivity : AppCompatActivity() {
    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        activityMenuSignIn.setOnClickListener {
            var message = Toast.makeText(applicationContext, "Logowanie", Toast.LENGTH_SHORT)
            message.show()

            val startMenuActivity = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(startMenuActivity)
        }

        activityMenuSignUp.setOnClickListener {
            var message =
                Toast.makeText(applicationContext, "Rejestrowanie", Toast.LENGTH_SHORT)
            message.show()

            val registrationMenuActivity = Intent(applicationContext, LoginActivity::class.java)
            startActivity(registrationMenuActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        isCurrentUser()
    }

    private fun isCurrentUser() {
        fbAuth.currentUser?.let {auth->
            val intent = Intent(applicationContext, BicycleActivity::class.java)
                .apply {
                    flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            startActivity(intent)
        }
    }
}