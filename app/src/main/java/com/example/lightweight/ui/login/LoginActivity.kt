package com.example.lightweight.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lightweight.*
import com.example.lightweight.R
import com.example.lightweight.classes.Preferences
import com.example.lightweight.ui.NavigationActivity
import com.example.lightweight.ui.Register.RegisterActivity
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var callbackManager: CallbackManager? = null
    private lateinit var textInputEmail: TextInputEditText
    private lateinit var textInputPassword: TextInputEditText
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var progressBar: ProgressBar
    private var prefs: Preferences? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppEventsLogger.activateApp(application)

        prefs = Preferences(this)
        val firstTime = prefs!!.firstTimeLogin

        callbackManager = CallbackManager.Factory.create()
        val fbLoginButton: LoginButton = findViewById(R.id.fbLogin_button)
        val userSignUp = findViewById<Button>(R.id.signUp_button)
        val userLogin = findViewById<Button>(R.id.loginButton_button)
        progressBar = findViewById(R.id.progressBarLogin)

        auth = FirebaseAuth.getInstance()

        userSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        userLogin.setOnClickListener {
            passwordSignIn()
        }

        fbLoginButton.setOnClickListener {
            fbSignIn()
        }


    }

    override fun onStart() {
        super.onStart()
        if (isAlreadyLoggedIn()){
            startActivity(Intent(this, NavigationActivity::class.java))
            finish()
        }
    }

    private fun isAlreadyLoggedIn(): Boolean{
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired){
            val profile = Profile.getCurrentProfile()
            Database.setUserId(profile.id)
            Database.updateUserDataFromFacebook(accessToken, prefs!!.firstTimeLogin)
            return true
        } else if (auth.currentUser != null){
            Database.setUserId(auth.currentUser!!.uid)
            Database.getUserInfoFromDb()
            return true
        }else
            return false
    }

    private fun passwordSignIn() {
        textInputEmail = findViewById(R.id.emailLogin_editText)
        textInputPassword = findViewById(R.id.passwordLogin_editText)


        email = textInputEmail.text.toString().trim().toLowerCase(Locale.ROOT)
        password = textInputPassword.text.toString().trim().toLowerCase(Locale.ROOT)

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
                    isAlreadyLoggedIn()
                    startActivity(Intent(this, NavigationActivity::class.java))
                    progressBar.visibility = View.INVISIBLE
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, getString(R.string.login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
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
                if (prefs!!.firstTimeLogin) {
                    prefs!!.firstTimeLogin = false
                    Database.updateUserDataFromFacebook(accessToken, firstTime = true)
                }else
                    Database.updateUserDataFromFacebook(accessToken, firstTime = prefs!!.firstTimeLogin)

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
