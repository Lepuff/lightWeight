package com.example.lightweight.ui.register

//import com.example.lightweight.Database.emailUser

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.Validation
import com.example.lightweight.ui.NavigationActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var textInputEmail: TextInputEditText
    private lateinit var textInputPassword: TextInputEditText
    private lateinit var textInputFirstName: TextInputEditText
    private lateinit var textInputLastName: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var textInputPasswordLayout: TextInputLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signUpButton = findViewById<Button>(R.id.registerButton_button)
        textInputEmail = findViewById(R.id.emailRegister_editText)
        textInputPassword = findViewById(R.id.passwordRegister_editText)
        textInputFirstName = findViewById(R.id.firstNameRegister_editText)
        textInputLastName = findViewById(R.id.lastNameRegister_editText)
        textInputPasswordLayout = findViewById(R.id.passwordRegister_layout)
        progressBar = findViewById(R.id.progressBarRegister)

        textInputFirstName.requestFocus()

        textInputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (textInputPasswordLayout.endIconMode != TextInputLayout.END_ICON_PASSWORD_TOGGLE)
                    textInputPasswordLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        signUpButton.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            signUpUser()
        }

    }


    private fun signUpUser() {
        val email = textInputEmail.text.toString().trim().toLowerCase(Locale.ROOT)
        val password = textInputPassword.text.toString().trim()
        val firstName = textInputFirstName.text.toString()
        val lastName = textInputLastName.text.toString()


        //check if firstName is empty
        if (firstName.isEmpty()) {
            textInputFirstName.error = getString(R.string.field_cant_be_empty)
            textInputFirstName.requestFocus()
            return
        }

        //check if lastName is empty
        if (lastName.isEmpty()) {
            textInputLastName.error = getString(R.string.field_cant_be_empty)
            textInputLastName.requestFocus()
            return
        }

        //check if email is empty or invalid
        if (!Validation.isValidEmail(email)) {
            if (email.isEmpty()) {
                textInputEmail.error = getString(R.string.field_cant_be_empty)
                textInputEmail.requestFocus()
                return
            } else {
                textInputEmail.error = getString(R.string.enter_valid_email)
                textInputEmail.requestFocus()
                return
            }
        }

        //check if password is empty or too short
        if (!Validation.isValidPassword(password)) {
            if (password.isEmpty()) {
                textInputPassword.error = getString(R.string.field_cant_be_empty)
                textInputPassword.requestFocus()
                textInputPasswordLayout.endIconMode = TextInputLayout.END_ICON_NONE
                return
            } else {
                textInputPassword.error = getString(R.string.password_too_short)
                textInputPassword.requestFocus()
                textInputPasswordLayout.endIconMode = TextInputLayout.END_ICON_NONE
                return
            }
        }

        progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val id = auth.currentUser!!.uid

                    Database.setUser(
                        id = id, isFacebookUser = false, email = email,
                        firstName = firstName, lastName = lastName, picture = null
                    )
                    Toast.makeText(this, getString(R.string.sign_up_successful), Toast.LENGTH_SHORT)
                        .show()

                    //update database with user data
                    Database.userInfoToDb()

                    startActivity(Intent(this, NavigationActivity::class.java))
                    progressBar.visibility = View.INVISIBLE
                    finish()
                } else {
                    Log.d("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        getString(R.string.authentication_failed), //TODO set message to $exception
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.INVISIBLE
                }

            }
    }
}


