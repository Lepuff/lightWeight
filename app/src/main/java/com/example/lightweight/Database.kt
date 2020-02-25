package com.example.lightweight

import com.facebook.AccessToken
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.time.LocalDateTime
import java.util.*

object Database{
    fun updateUserData(accessToken: AccessToken, firstName: String, lastName: String, email: String){
        val db = FirebaseFirestore.getInstance()

        val user = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email
        )
        // Add a new document with a email-adress as ID
        db.collection("users").document(email)
            .set(user)
    }

    fun addWorkout(accessToken: AccessToken, email: String){
        val db = FirebaseFirestore.getInstance()

        val weight: Int? = null
        val reps: Int? = null
        val sets: Int? = null
        val date: LocalDateTime = LocalDateTime.now()
        val typeOfExercise: String? = null

        val gymExercise = hashMapOf(
            "reps" to reps,
            "sets" to sets,
            "weight" to weight
        )

        db.collection("users").document(email)
            .collection("workouts").document(date.toString() + typeOfExercise)
    }
}