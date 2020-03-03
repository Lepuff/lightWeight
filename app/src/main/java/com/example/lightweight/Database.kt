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

    var facebookUser: User = User("", "", "")
    var emailUser: User = User("", "", "")


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

    }
*/
}