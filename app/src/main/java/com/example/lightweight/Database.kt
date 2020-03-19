package com.example.lightweight

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Picture
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.net.toUri
import com.example.lightweight.classes.User
import com.example.lightweight.ui.Profile.ProfileFragment
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.Profile
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage


object Database {

    //private var profilePicture: Uri = Uri.parse("android.resource://com.example.lightweight/drawable/@mipmap/ic_launcher_round")
    private var user: User = User(null, true, null, null, null, null)

    const val WORKOUTS = "workouts"
    const val USERS = "users"
    const val FRIENDS = "friends"
    const val AVERAGE_PULSE = "averagePulse"
    const val AVERAGE_SPEED = "averageSpeed"
    const val CALORIES = "calories"
    const val DISTANCE = "distance"
    const val NAME ="name"
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
    const val EXERCISES = "exercises"
    const val TYPE_OF_EXERCISE = "typeOfExercise"
    const val SETS = "sets"
    const val REPS = "reps"
    const val WEIGHT = "weight"
    const val EMAIL = "email"
    const val ID = "id"
    const val FIRST_NAME = "firstName"
    const val LAST_NAME = "lastName"
    const val PICTURE_URI = "pictureUri"




    fun getUser(): User{
        return user
    }

    fun isFacebookUser(): Boolean {
        return user.isFacebookUser
    }

    fun setUser(id: String?, isFacebookUser: Boolean, email: String?, firstName: String?, lastName: String?, picture: Uri?){
        user.id = id
        user.isFacebookUser = isFacebookUser
        user.email = email
        user.firstName = firstName
        user.lastName = lastName
        user.profilePicture = picture
    }

    fun getUserPicture(): Uri?{
        return user.profilePicture
    }

    fun getUserPictureFromDb(): Uri?{
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection(USERS).document(user.id!!).get()
        userRef.addOnSuccessListener { document ->
            user.profilePicture = document["pictureUri"].toString().toUri()
        }
        return user.profilePicture
    }

    fun setUserPicture(newPicture: Uri?){
        val db = FirebaseFirestore.getInstance()
        val mStorageRef = FirebaseStorage.getInstance().getReference("profilePictures")
        val imageReference = mStorageRef.child(getUserId()!!)

        imageReference.putFile(newPicture!!)
            .addOnSuccessListener {taskSnapshot ->
                //Add image to user and db
                user.profilePicture = newPicture
                db.collection(USERS).document(getUserId()!!)
                    .update("pictureUri", taskSnapshot.storage.downloadUrl.toString())
            }
            .addOnFailureListener { e ->
                Log.d("TAG", e.message!!)
            }
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

    fun updateUserEmail(newEmail: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser!!.updateEmail(newEmail)
        user.email = newEmail
    }

    fun getUserFirstName(): String? {
        return user.firstName
    }

    fun getUserName():  String{
        return user.firstName +" "+ user.lastName
    }

    fun updateUserFirstName(newFirstName: String){
        val db = FirebaseFirestore.getInstance()
        user.firstName = newFirstName
        db.collection(USERS).document(getUserId()!!).update("firstName", newFirstName)
    }

    fun getUserLastName(): String? {
        return user.lastName
    }

    fun updateUserLastName(newLastName: String){
        val db = FirebaseFirestore.getInstance()
        user.lastName = newLastName
        db.collection(USERS).document(getUserId()!!).update("lastName", newLastName)
    }

    fun getUserInfoFromDb(){
        val db = FirebaseFirestore.getInstance()
        db.collection(USERS).document(getUserId()!!).get()
            .addOnSuccessListener {document ->
            if (document != null){
                user.email = document["email"].toString()
                user.firstName = document["firstName"].toString()
                user.lastName = document["lastName"].toString()
                user.profilePicture = document["pictureUri"].toString().toUri()
                user.isFacebookUser = document["isFacebookUser"].toString().toBoolean()
            }
        }
    }

    fun userInfoToDb() {
        val db = FirebaseFirestore.getInstance()
        val userInfo = hashMapOf(
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "email" to user.email,
            "id" to user.id,
            "pictureUri" to user.profilePicture.toString(),
            "isFacebookUser" to user.isFacebookUser
        )
        db.collection(USERS).document(getUserId()!!)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: $documentReference")

            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    fun updateUserDataFromFacebook(accessToken: AccessToken?) {
            val request = GraphRequest.newMeRequest(
                accessToken
            ) { `object`, response ->

                user.id = `object`.getString("id")
                user.email = `object`.getString("email")
                user.firstName = `object`.getString("first_name")
                user.lastName = `object`.getString("last_name")
                if (getUserPictureFromDb() == null)
                    user.profilePicture = Profile.getCurrentProfile().getProfilePictureUri(120, 120)
                else
                    user.profilePicture = getUserPictureFromDb()
                user.isFacebookUser = true

                userInfoToDb()
            }
            //Here we put the requested fields to be returned from the JSONObject
            val parameters = Bundle()
            parameters.putString("fields", "id, first_name, last_name, email, picture")
            request.parameters = parameters
            request.executeAsync()

    }
}