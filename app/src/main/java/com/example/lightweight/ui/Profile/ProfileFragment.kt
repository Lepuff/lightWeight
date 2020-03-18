package com.example.lightweight.ui.Profile

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.Database

import com.example.lightweight.R

import com.example.lightweight.ui.login.LoginActivity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView


import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.*
import java.net.URL

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private val PICK_PHOTO_REQUEST = 1
    private lateinit var mImageUri: Uri
    private lateinit var mStorageRef: StorageReference
    private lateinit var db: FirebaseFirestore
    private lateinit var profilePicture: CircleImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val logoutButton = root.findViewById<Button>(R.id.profile_logout_button)
        mStorageRef = FirebaseStorage.getInstance().getReference("profilePictures")
        db = FirebaseFirestore.getInstance()
        profilePicture = root.findViewById<CircleImageView>(R.id.profile_image)

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()//sign out user
            LoginManager.getInstance().logOut()
            AccessToken.setCurrentAccessToken(null)
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }
        root.findViewById<TextView>(R.id.fragment_profile_first_name_edittext).text = Database.getUserFirstName()
        root.findViewById<TextView>(R.id.fragment_profile_last_name_edittext).text = Database.getUserLastName()
        root.findViewById<TextView>(R.id.fragment_profile_email_edittext).text = Database.getUserEmail()


        if (Database.isFacebookUser()) {
            root.findViewById<Button>(R.id.profile_change_password_button).isEnabled = false
            root.findViewById<Button>(R.id.profile_edit_profile_button).isEnabled = false
        }
 
        //get profile pic from database and load into glide
        val a = mStorageRef.child(Database.getUserId()!!).downloadUrl.addOnSuccessListener {task ->
            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error_layer)
                .fallback(R.drawable.ic_fallback_foreground)

            Glide.with(this)
                .applyDefaultRequestOptions(requestOption)
                .load(task)
                .into(profilePicture)
        }

        //updateGlidePicture(Database.getUserPicture().toString().toUri().toFile())


        profilePicture.setOnClickListener {
            pickPhotoFromGallery()
        }

        val editProfile = root.findViewById<Button>(R.id.profile_edit_profile_button)
        editProfile.setOnClickListener {
            //todo
        }

        val changePassword = root.findViewById<Button>(R.id.profile_change_password_button)
        changePassword.setOnClickListener {
            //todo
        }

        return root

    }

    private fun updateGlidePicture(imageUri: Uri){

        val requestOption = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_error_layer)
            .fallback(R.drawable.ic_fallback_foreground)

        Glide.with(this)
            .applyDefaultRequestOptions(requestOption)
            .load(imageUri)
            .into(profilePicture)
    }


    private fun pickPhotoFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_PHOTO_REQUEST)
    }

    fun uploadImageToDb() {
        val imageReference = mStorageRef.child(Database.getUserId()!!)

        imageReference.putFile(mImageUri)
            .addOnSuccessListener {taskSnapshot ->
                Toast.makeText(this.context, "Upload successful", Toast.LENGTH_LONG).show()
                //Add image to db
                Database.setUserPicture(imageReference.downloadUrl.toString())
                db.collection(Database.USERS).document(Database.getUserId()!!)
                    .update("pictureUri", taskSnapshot.storage.downloadUrl.toString()) //imageReference.downloadUrl.toString()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this.context, e.message, Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_PHOTO_REQUEST &&
            data != null && data.data != null
        ) {
            mImageUri = data.data!!
            updateGlidePicture(mImageUri)
            uploadImageToDb()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }


}