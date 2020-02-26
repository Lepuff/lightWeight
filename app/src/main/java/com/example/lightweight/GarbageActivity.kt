package com.example.lightweight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lightweight.ui.login.LoginActivity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class GarbageActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garbage)
        auth = FirebaseAuth.getInstance()


        // Check if user is signed in (non-null), else send to LoginActivity
        //val currentUser = auth.currentUser
    }


    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut() //sign out user
        LoginManager.getInstance().logOut()
        AccessToken.setCurrentAccessToken(null)
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }
}

