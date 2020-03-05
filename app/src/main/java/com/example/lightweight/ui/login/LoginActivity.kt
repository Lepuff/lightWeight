package com.example.lightweight.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lightweight.*
//import com.example.lightweight.Database.getUserDetailsFromFb
import com.example.lightweight.R
import com.example.lightweight.ui.NavigationActivity
import com.example.lightweight.ui.Register.RegisterActivity
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import java.net.URL


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var callbackManager: CallbackManager? = null
    private lateinit var textInputEmail: TextInputEditText
    private lateinit var textInputPassword: TextInputEditText
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var profilePicture: URL
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppEventsLogger.activateApp(application)


        callbackManager = CallbackManager.Factory.create()
        val fbLoginButton: LoginButton = findViewById(R.id.fbLogin_button)
        val userSignUp = findViewById<Button>(R.id.signUp_button)
        val userLogin = findViewById<Button>(R.id.loginButton_button)
        progressBar = findViewById(R.id.progressBarLogin)

        auth = FirebaseAuth.getInstance()

        userSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        userLogin.setOnClickListener {
            passwordSignIn()

        }

        fbLoginButton.setOnClickListener {
            //fbLoginButton.setPermissions(listOf("email", "public_profile", "user_friends"))
            fbSignIn()

        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn: Boolean = accessToken != null && !accessToken.isExpired
        updateUI(currentUser)
        updateUI(isLoggedIn)


    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, NavigationActivity::class.java))
            finish()
        }
        return
    }

    private fun updateUI(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            startActivity(Intent(applicationContext, NavigationActivity::class.java))
            finish()
        }
        return
    }

    private fun passwordSignIn() {
        textInputEmail = findViewById(R.id.emailLogin_editText)
        textInputPassword = findViewById(R.id.passwordLogin_editText)

        email = textInputEmail.text.toString().trim()
        password = textInputPassword.text.toString().trim()

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
        progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                    progressBar.visibility = View.INVISIBLE
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Login failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                    progressBar.visibility = View.INVISIBLE
                }

            }
    }

    private fun fbSignIn() {
        fbLogin_button.setPermissions(listOf("email", "public_profile", "user_friends"))
        fbLogin_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(error: FacebookException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //get credential
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)

        progressBar.visibility = View.VISIBLE
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                Database.addFacebookUserToDb(accessToken)

                Toast.makeText(this, "Log in successful", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, NavigationActivity::class.java))
                progressBar.visibility = View.INVISIBLE
            }

            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }
}
