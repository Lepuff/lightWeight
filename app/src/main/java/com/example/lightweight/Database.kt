package com.example.lightweight

import android.os.Bundle
import android.util.Log
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.time.LocalDateTime
import java.util.*



object Database{


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
/*
    fun addExercise(email: String, exerciseID: AbstractWorkout) {
        val db = FirebaseFirestore.getInstance()

        when(exerciseID){
            is GymWorkout ->{
                val gymExercise = hashMapOf(
                    "weight" to exerciseID.sets.weight,
                    "reps" to exerciseID.sets.reps
            }

        }

        db.collection("users").document(email)
            .collection("workouts").document("workoutID").collection(exerciseID)
            .document(exerciseID.sets.number)
            .set(gymExercise)

    }*/
}