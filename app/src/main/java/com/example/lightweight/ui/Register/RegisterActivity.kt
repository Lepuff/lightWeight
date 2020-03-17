package com.example.lightweight.ui.Register

//import com.example.lightweight.Database.emailUser

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.Validation
import com.example.lightweight.ui.NavigationActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var textInputEmail: TextInputEditText
    private lateinit var textInputPassword: TextInputEditText
    private lateinit var textInputFirstName: TextInputEditText
    private lateinit var textInputLastName: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signUpButton = findViewById<Button>(R.id.registerButton_button)


        progressBar = findViewById(R.id.progressBarRegister)


        signUpButton.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            signUpUser()
        }

    }

    private fun signUpUser() {
        textInputEmail = findViewById(R.id.emailRegister_editText)
        textInputPassword = findViewById(R.id.passwordRegister_editText)
        textInputFirstName = findViewById(R.id.firstNameRegister_editText)
        textInputLastName = findViewById(R.id.lastNameRegister_editText)

        val email = textInputEmail.text.toString().trim().toLowerCase(Locale.ROOT)
        val password = textInputPassword.text.toString().trim()
        val firstName = textInputFirstName.text.toString()
        val lastName = textInputLastName.text.toString()


        if (Validation.isFieldEmpty(firstName)){
            textInputFirstName.error = getString(R.string.field_cant_be_empty)
            textInputFirstName.requestFocus()
            return
        }

        if (Validation.isFieldEmpty(lastName)) {
            textInputLastName.error = getString(R.string.field_cant_be_empty)
            textInputLastName.requestFocus()
            return
        }

        if (!Validation.isValidEmail(email)) {
            if (Validation.isFieldEmpty(email)) {
                textInputEmail.error = getString(R.string.field_cant_be_empty)
                textInputEmail.requestFocus()
                return
            } else {
                textInputEmail.error = getString(R.string.enter_valid_email)
                textInputEmail.requestFocus()
                return
            }
        }

        if (!Validation.isValidPassword(password)) {
            if (Validation.isFieldEmpty(password)) {
                textInputPassword.error = getString(R.string.field_cant_be_empty)
                textInputPassword.requestFocus()
                return
            } else {
                textInputPassword.error = getString(R.string.password_too_short)
                textInputPassword.requestFocus()
                return
            }
        }

        progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val id = auth.currentUser!!.uid

                    Database.setUser(id = id, isFacebookUser = false, email = email,
                        firstName = firstName, lastName = lastName, picture = null)
                    Toast.makeText(this, getString(R.string.sign_up_successful), Toast.LENGTH_SHORT)
                        .show()

                    //update database with user data
                    Database.updateUserData(null)

                    startActivity(Intent(this, NavigationActivity::class.java))
                    progressBar.visibility = View.INVISIBLE
                    finish()
                } else {
                    Log.d("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, getString(R.string.authentication_failed), //TODO set message to $exception
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.INVISIBLE
                }

            }
    }
}


