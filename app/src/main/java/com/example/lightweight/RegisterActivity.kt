package com.example.lightweight

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import kotlin.collections.ArrayList


class RegisterActivity : AppCompatActivity() {

    private lateinit var textInputEmail: TextInputEditText
    private lateinit var textInputPassword: TextInputEditText
    private lateinit var textInputFirstName: TextInputEditText
    private lateinit var textInputLastName: TextInputEditText
    private lateinit var progressBar: ProgressBar

    private lateinit var auth: FirebaseAuth
    // Access a Cloud Firestore instance from your Activity
    //private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signUpButton = findViewById<Button>(R.id.registerButton_button)


        progressBar = findViewById(R.id.progressBarRegister)

        auth = FirebaseAuth.getInstance()

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

        val email = textInputEmail.text.toString().trim()
        val password = textInputPassword.text.toString().trim()
        val firstName = textInputFirstName.text.toString().trim()
        val lastName = textInputLastName.text.toString().trim()

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()

        if (Validation.isFieldEmpty(firstName)) {
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
                    Toast.makeText(this, getString(R.string.sign_up_successful), Toast.LENGTH_SHORT)
                        .show()
                    //create user
                    val user = hashMapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email,
                        "password" to password
                    )

                    // Add a new document with a generated ID
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("TAG", "Error adding document", e)
                        }
                    startActivity(Intent(this, LoginActivity::class.java))
                    progressBar.visibility = View.INVISIBLE
                    finish()
                } else {
                    Toast.makeText(
                        baseContext, getString(R.string.sign_up_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.INVISIBLE
                }

            }
    }

    /*private fun isFieldEmpty(target: CharSequence): Boolean {
        return TextUtils.isEmpty(target)
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun isValidPassword(target: CharSequence?): Boolean {
        return target!!.length > 5
    }
    */


}


