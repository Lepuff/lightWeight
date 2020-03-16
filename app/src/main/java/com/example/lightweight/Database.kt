package com.example.lightweight

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.example.lightweight.classes.User
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.Profile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.net.URI
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap
import kotlin.properties.Delegates


object Database {

    //private var profilePicture: Uri = Uri.parse("android.resource://com.example.lightweight/drawable/@mipmap/ic_launcher_round")
    private var user: User = User(null, true, null, null, null, null)


    const val WORKOUTS = "workouts"
    const val USERS = "users"





    const val AVERAGE_PULSE = "averagePulse"
    const val AVERAGE_SPEED = "averageSpeed"
    const val CALORIES = "calories"
    const val DISTANCE = "distance"

    const val MAX_PULSE = "maxPulse"

    const val TIMESTAMP = "timestamp"

    const val TOP_SPEED = "topSpeed"

    const val TOTAL_TIME = "totalTime"

    const val TYPE_OF_WORKOUT = "typeOfWorkout"

    const val WORKOUT_DATE = "workoutDate"

    const val WORKOUT_TITLE = "workoutTitle"

    const val AVERAGE_FORCE = "averageForce"

    const val MAX_FORCE = "maxForce"
    const val AVERAGE_CADENCE = "averageCadence"
    const val MAX_CADENCE = "maxCadence"


    fun getUser(): User{
        return user
    }

    fun setUser(id: String, isFacebookUser: Boolean, email: String, firstName: String, lastName: String){
        user.id = id
        user.isFacebookUser = isFacebookUser
        user.email = email
        user.firstName = firstName
        user.lastName = lastName
    }

    fun getUserPicture(): Uri{
        return user.profilePicture!!
    }

    fun getUserId(): String? {
        return user.id
    }

    fun setUserId(newId: String){
        user.id = newId
    }

    fun getUserEmail(): String? {
        return user.email
    }

    fun setUserEmail(newEmail: String) {
        user.email = newEmail
        userInfoToDb()
    }

    fun getUserFirstName(): String? {
        return user.firstName
    }

    fun setUserFirstName(newFirstName: String){
        user.firstName = newFirstName
        userInfoToDb()
    }

    fun getUserLastName(): String? {
        return user.lastName
    }

    fun setUserLastName(newLastName: String){
        user.lastName = newLastName
        userInfoToDb()
    }

    private fun userInfoToDb() {
        val userInfo = hashMapOf(
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "email" to user.email,
            "id" to user.id,
            "pictureUri" to user.profilePicture.toString()
        )
        val db = FirebaseFirestore.getInstance()
        db.collection(USERS).document(getUserId()!!)
            .set(userInfo, SetOptions.merge())
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

                user.id = `object`.getString("id")
                user.email = `object`.getString("email")
                user.firstName = `object`.getString("first_name")
                user.lastName = `object`.getString("last_name")
                user.profilePicture = Profile.getCurrentProfile().getProfilePictureUri(120, 120)

                userInfoToDb()
            }
            //Here we put the requested fields to be returned from the JSONObject
            val parameters = Bundle()
            parameters.putString("fields", "id, first_name, last_name, email, picture")
            request.parameters = parameters
            request.executeAsync()
        } else {
            userInfoToDb()
        }
    }
}