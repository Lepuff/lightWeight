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
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var textInputEmail: TextInputEditText
    private lateinit var textInputPassword: TextInputEditText
    private lateinit var textInputFirstName: TextInputEditText
    private lateinit var textInputLastName: TextInputEditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signUpButton = findViewById<Button>(R.id.registerButton_button)


        auth = FirebaseAuth.getInstance()
        signUpButton.setOnClickListener {
            signUpUser()
        }


    }

    private fun signUpUser() {
        textInputEmail = findViewById(R.id.emailRegister_editText)
        textInputPassword = findViewById(R.id.passwordRegister_editText)
        textInputFirstName = findViewById(R.id.firstNameRegister_editText)
        textInputLastName = findViewById(R.id.lastNameRegister_editText)

        var email = textInputEmail.text.toString().trim()
        var password = textInputPassword.text.toString().trim()
        var firstName = textInputFirstName.text.toString().trim()
        var lastName = textInputLastName.text.toString().trim()

        if (!isValidEmail(email)) {
            textInputEmail.error = "Enter a valid email"
            textInputEmail.requestFocus()
            return

        }
        if (!isValidPassword(password)) {
            textInputPassword.error = "Enter a password"
            textInputPassword.requestFocus()
            return

        }
        progressBarRegister.isShown
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign up Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    progressBarRegister.isShown.not()
                    finish()
                }
                else {
                    Toast.makeText(
                        baseContext, "Sign up failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }


    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun isValidPassword(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && target!!.length > 5
    }
}
