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
import kotlin.collections.HashMap
import kotlin.properties.Delegates


object Database {

    var user: User = User(true, null, null, null)



    fun getUserEmail(): String? {
        return user.email
    }


    private fun userInfoToDb() {
        val userInfo = hashMapOf(
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "email" to user.email
        )
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(getUserEmail()!!)
            .set(userInfo)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    fun updateUserData(accessToken: AccessToken?) {
        if (user.isFacebookUser) {
            val request = GraphRequest.newMeRequest(
                accessToken
            ) { `object`, response ->

                user.email = `object`.getString("email")
                user.firstName = `object`.getString("first_name")
                user.lastName = `object`.getString("last_name")

                userInfoToDb()
            }
            //Here we put the requested fields to be returned from the JSONObject
            val parameters = Bundle()
            parameters.putString("fields", "id, first_name, last_name, email")
            request.parameters = parameters
            request.executeAsync()
        } else
            userInfoToDb()
    }
}