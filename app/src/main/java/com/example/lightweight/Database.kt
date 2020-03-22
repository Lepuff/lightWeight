package com.example.lightweight

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import com.example.lightweight.classes.User
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage


object Database {

    private var user: User = User(null, true, null, null, null, null)

    //const val SP_INFO = "SP_INFO" //shared preferences info
    const val RUNNING_WORKOUT = "runningWorkout"
    const val GYM_WORKOUT = "gymWorkout"
    const val CYCLING_WORKOUT = "cyclingWorkout"
    const val WORKOUTS = "workouts"
    const val USERS = "users"
    const val FRIENDS = "friends"
    const val AVERAGE_PULSE = "averagePulse"
    const val AVERAGE_SPEED = "averageSpeed"
    const val CALORIES = "calories"
    const val DISTANCE = "distance"
    const val NAME = "name"
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
    const val PROFILE_PICTURES = "profilePictures"
    const val IS_FACEBOOK_USER = "isFacebookUser"
    const val PICTURE_SIZE = 120

    fun isFacebookUser(): Boolean {
        return user.isFacebookUser
    }

    fun getUserInfoFromDb() {
        val db = FirebaseFirestore.getInstance()
        db.collection(USERS).document(getUserId()!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user.email = document[EMAIL].toString()
                    user.firstName = document[FIRST_NAME].toString()
                    user.lastName = document[LAST_NAME].toString()
                    user.profilePicture = document[PICTURE_URI].toString().toUri()
                    user.isFacebookUser = document[IS_FACEBOOK_USER].toString().toBoolean()
                }
            }
    }

    fun userInfoToDb() {
        val db = FirebaseFirestore.getInstance()
        val userInfo = hashMapOf(
            FIRST_NAME to user.firstName,
            LAST_NAME to user.lastName,
            EMAIL to user.email,
            ID to user.id,
            PICTURE_URI to user.profilePicture.toString(),
            IS_FACEBOOK_USER to user.isFacebookUser
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
            user.id = `object`.getString(ID)
            user.email = `object`.getString(EMAIL)
            user.firstName = `object`.getString(FIRST_NAME)
            user.lastName = `object`.getString(LAST_NAME)
            user.isFacebookUser = true
            user.profilePicture =
                Profile.getCurrentProfile().getProfilePictureUri(PICTURE_SIZE, PICTURE_SIZE)
            userInfoToDb()
        }
        //Here we put the requested fields to be returned from the JSONObject
        val parameters = Bundle()
        parameters.putString("fields", "id, first_name, last_name, email, picture")
        request.parameters = parameters
        request.executeAsync()
    }

    fun setUser(
        id: String?,
        isFacebookUser: Boolean,
        email: String?,
        firstName: String?,
        lastName: String?,
        picture: Uri?
    ) {
        user.id = id
        user.isFacebookUser = isFacebookUser
        user.email = email
        user.firstName = firstName
        user.lastName = lastName
        user.profilePicture = picture
    }

    fun updateUser(firstName: String?, lastName: String?, email: String?) {
        val db = FirebaseFirestore.getInstance()
        if (firstName != null)
            user.firstName = firstName
        if (lastName != null)
            user.lastName = lastName
        if (email != null) {
            user.email = email
            FirebaseAuth.getInstance().currentUser!!.updateEmail(email)
        }

        db.collection(USERS).document(getUserId()!!)
            .update(FIRST_NAME, user.firstName, LAST_NAME, user.lastName, EMAIL, email)
    }

    fun getUserPicture(): Uri? {
        return user.profilePicture
    }

    fun setUserPicture(newPicture: Uri?) {
        val mStorageRef = FirebaseStorage.getInstance().getReference(PROFILE_PICTURES)
        val imageReference = mStorageRef.child(getUserId()!!)

        imageReference.putFile(newPicture!!)
            .addOnSuccessListener {
                Log.d("ProfileFragment", "Successfully uploaded image: ${it.metadata?.path}")
                imageReference.downloadUrl.addOnSuccessListener { imageLocation ->
                    Log.d("ProfileFragment", "File location $imageLocation")
                    user.profilePicture = imageLocation
                    val db = FirebaseFirestore.getInstance()
                    db.collection(USERS).document(getUserId()!!)
                        .update(PICTURE_URI, imageLocation.toString())
                }
            }
    }

    fun getUserId(): String? {
        return user.id
    }

    fun setUserId(newId: String) {
        user.id = newId
    }

    fun getUserEmail(): String? {
        return user.email
    }

    fun getUserFullName(): String {
        return user.firstName + " " + user.lastName
    }

    fun getUserFirstName(): String? {
        return user.firstName
    }

    fun getUserLastName(): String? {
        return user.lastName
    }
}