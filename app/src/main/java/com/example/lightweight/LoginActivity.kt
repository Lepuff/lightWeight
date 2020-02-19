package com.example.lightweight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var callbackManager: CallbackManager? = null
    private lateinit var textInputEmail: TextInputEditText
    private lateinit var textInputPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        callbackManager = CallbackManager.Factory.create()
        val fbLoginButton: LoginButton = findViewById(R.id.fbLogin_button)
        val userSignUp = findViewById<Button>(R.id.signUp_button)
        val userLogin = findViewById<Button>(R.id.loginButton_button)

        fbLoginButton.setPermissions(listOf("email", "public_profile", "user_friends"))

        auth = FirebaseAuth.getInstance()

        userSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        userLogin.setOnClickListener {
            passwordSignIn()

        }

        fbLoginButton.setOnClickListener {
            fbSignIn()

        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)

    }

    private fun updateUI(currentUser: FirebaseUser?){
        if (currentUser != null){
            startActivity(Intent(this, GarbageActivity::class.java))
            finish()
        }
    }

    private fun passwordSignIn(){
        textInputEmail = findViewById(R.id.emailLogin_editText)
        textInputPassword = findViewById(R.id.passwordLogin_editText)

        val email = textInputEmail.text.toString().trim()
        val password = textInputPassword.text.toString().trim()

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
            }
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

            }
    }

    private fun fbSignIn() {
        fbLogin_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }

        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //get credential
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                Toast.makeText(this, "Log in successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, GarbageActivity::class.java))
            }

            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }
}
