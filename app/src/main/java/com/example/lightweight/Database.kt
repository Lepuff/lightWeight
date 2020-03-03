package com.example.lightweight

import android.os.Bundle
import android.util.Log
import com.example.lightweight.classes.User
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.time.LocalDateTime
import java.util.*




object Database{

    var facebookUser: User = User(null, null, null)
    var emailUser: User = User(null, null, null)

    fun updateUserData(target: User) {
        val db = FirebaseFirestore.getInstance()

        val user = hashMapOf(
            "firstName" to target.firstName,
            "lastName" to target.lastName,
            "email" to target.email
        )
        // Add a new document with a email-address as ID
        db.collection("users").document(target.email!!)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    fun addFacebookUserToDb(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { `object`, response ->

            facebookUser.email = `object`.getString("email")
            facebookUser.firstName = `object`.getString("first_name")
            facebookUser.lastName = `object`.getString("last_name")

            updateUserData(facebookUser)
        }
        //Here we put the requested fields to be returned from the JSONObject
        val parameters = Bundle()
        parameters.putString("fields", "id, first_name, last_name, email")
        request.parameters = parameters
        request.executeAsync()
    }
}