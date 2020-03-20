package com.example.lightweight

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Picture
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.FileInputStream


object Database {


    private var user: User = User(null, true, null, null, null, null)


    const val SP_INFO = "SP_INFO" //shared preferences info
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

    fun getUserPictureFromDb(){
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection(USERS).document(user.id!!).get()
        userRef.addOnSuccessListener { document ->
            val a = document["pictureUri"]
            user.profilePicture = document["pictureUri"].toString().toUri()//todo constants
        }
    }

    fun setUserPicture(newPicture: Uri?){
        val mStorageRef = FirebaseStorage.getInstance().getReference("profilePictures")//todo constants
        val imageReference = mStorageRef.child(getUserId()!!)

        imageReference.putFile(newPicture!!)
            .addOnSuccessListener {
                Log.d("ProfileFragment", "Successfully uploaded image: ${it.metadata?.path}")
                imageReference.downloadUrl.addOnSuccessListener { imageLocation ->
                    Log.d("ProfileFragment", "File location $imageLocation")//todo constants
                    user.profilePicture = imageLocation
                    val db = FirebaseFirestore.getInstance()
                    db.collection(USERS).document(getUserId()!!)
                        .update("pictureUri", imageLocation.toString())//todo constants
                }
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

    fun updateUser(firstName: String?, lastName: String?, email: String?){
        val db = FirebaseFirestore.getInstance()
        if (firstName != null)
            user.firstName = firstName
        if (lastName != null)
            user.lastName = lastName
        if (email != null)
            user.email = email

        db.collection(USERS).document(getUserId()!!)
            .update("firstName", user.firstName, "lastName", user.lastName, "email", email)//todo constants
    }

    fun getUserFirstName(): String? {
        return user.firstName
    }

    fun getUserName():  String{
        return user.firstName +" "+ user.lastName
    }

    fun getUserLastName(): String? {
        return user.lastName
    }

    fun getUserInfoFromDb(){
        val db = FirebaseFirestore.getInstance()
        db.collection(USERS).document(getUserId()!!).get()
            .addOnSuccessListener {document ->
            if (document != null){
                user.email = document["email"].toString()//todo constants
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
            "firstName" to user.firstName,//todo constants
            "lastName" to user.lastName,
            "email" to user.email,
            "id" to user.id,
            "pictureUri" to user.profilePicture.toString(),
            "isFacebookUser" to user.isFacebookUser
        )
        db.collection(USERS).document(getUserId()!!)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: $documentReference")//todo tag should be a context for error handeling

            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    fun updateUserDataFromFacebook(accessToken: AccessToken?, firstTime: Boolean) {
            //val db = FirebaseFirestore.getInstance()
            val request = GraphRequest.newMeRequest(
                accessToken
            ) { `object`, response ->
                user.id = `object`.getString("id") //todo constants
                user.email = `object`.getString("email")
                user.firstName = `object`.getString("first_name")
                user.lastName = `object`.getString("last_name")
                //if (firstTime){
                user.profilePicture = Profile.getCurrentProfile().getProfilePictureUri(120, 120)
                //} else
                    //getUserPictureFromDb()
                /*user.isFacebookUser = true
                val userInfo = hashMapOf(
                    "firstName" to user.firstName,//todo constants
                    "lastName" to user.lastName,
                    "email" to user.email,
                    "id" to user.id,
                    "pictureUri" to user.profilePicture.toString(),
                    "isFacebookUser" to user.isFacebookUser
                )

                db.collection(USERS).document(user.id!!).set(userInfo, SetOptions.merge())*/
                userInfoToDb()
            }
            //Here we put the requested fields to be returned from the JSONObject
            val parameters = Bundle()
            parameters.putString("fields", "id, first_name, last_name, email, picture")//todo constants?
            request.parameters = parameters
            request.executeAsync()
    }
}