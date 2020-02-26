package com.example.lightweight

import android.os.Bundle
import android.util.Log
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.time.LocalDateTime
import java.util.*

object Database {
    /*fun getUserDetailsFromFb(accessToken: AccessToken, _firstName: String, _lastName: String, _email: String) {
        var firstName = _firstName
        var lastName = _lastName
        var email = _email
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { `object`, response ->

            email = `object`.getString("email")
            firstName = `object`.getString("first_name")
            lastName = `object`.getString("last_name")

            //Database.updateUserData(accessToken, firstName, lastName, email)
        }
        //Here we put the requested fields to be returned from the JSONObject
        val parameters = Bundle()
        parameters.putString("fields", "id, first_name, last_name, email")
        request.parameters = parameters
        request.executeAsync()
    }*/

    var numberOfSets: Int? = null
    fun updateUserData(firstName: String, lastName: String, email: String) {
        val db = FirebaseFirestore.getInstance()

        val user = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email
        )
        // Add a new document with a email-address as ID
        db.collection("users").document(email)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    fun addExercise(email: String, exerciseID: Exercise) {
        val db = FirebaseFirestore.getInstance()

        val gymExercise = hashMapOf(
            "weight" to exerciseID.sets.weight,
            "reps" to exerciseID.sets.reps
        )

        db.collection("users").document(email)
            .collection("workouts").document("workoutID").collection(exerciseID)
            .document(exerciseID.sets.number)
            .set(gymExercise)

    }
}