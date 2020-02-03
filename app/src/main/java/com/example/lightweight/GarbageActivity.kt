package com.example.lightweight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth

class GarbageActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garbage)

        mAuth = FirebaseAuth.getInstance()

        //val currentUser = FirebaseAuth.getInstance().currentUser

        }

    /*override fun onStart() {
        super.onStart()

        //if user is already logged in, send to Garbage activity    **
        if(mAuth.currentUser != null){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }*/

    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut() //sign out user
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

