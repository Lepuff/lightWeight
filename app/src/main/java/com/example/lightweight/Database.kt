package com.example.lightweight

import com.facebook.AccessToken
import com.google.firebase.firestore.FirebaseFirestore

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
}