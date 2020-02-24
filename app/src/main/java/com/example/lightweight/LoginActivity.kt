package com.example.lightweight

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var callbackManager: CallbackManager? = null
    private lateinit var textInputEmail: TextInputEditText
    private lateinit var textInputPassword: TextInputEditText
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var profilePicture: URL


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppEventsLogger.activateApp(application)

        callbackManager = CallbackManager.Factory.create()
        val fbLoginButton: LoginButton = findViewById(R.id.fbLogin_button)
        val userSignUp = findViewById<Button>(R.id.signUp_button)
        val userLogin = findViewById<Button>(R.id.loginButton_button)

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
            startActivity(Intent(this, GarbageActivity::class.java))
            finish()
        }
    }

    private fun updateUI(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            startActivity(Intent(this, GarbageActivity::class.java))
            finish()
        }
    }

    private fun passwordSignIn() {
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
                    Toast.makeText(
                        baseContext, "Login failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

            }
    }

    private fun fbSignIn() {
        fbLogin_button.setPermissions(listOf("email", "public_profile", "user_friends"))
        fbLogin_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result!!.accessToken)
            }



            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(error: FacebookException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun getUserDetailsFromFb(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { `object`, response ->

            email = `object`.getString("email")
            firstName = `object`.getString("first_name")
            lastName = `object`.getString("last_name")

            Database.updateUserData(accessToken, firstName, lastName, email)


            /*val db = FirebaseFirestore.getInstance()
            //create user

            val user = hashMapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "email" to email
            )

            // Add a new document with a email-adress as ID
            db.collection("users").document(email)
                .set(user)*/
        }
        //Here we put the requested fields to be returned from the JSONObject
        val parameters = Bundle()
        parameters.putString("fields", "id, first_name, last_name, email")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //get credential
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)

        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                getUserDetailsFromFb(accessToken)
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
